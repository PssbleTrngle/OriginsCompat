package com.possible_triangle.origins_compat.api;

import com.possible_triangle.origins_compat.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class WaterTankSources {

    private static final List<Function<LivingEntity, List<ItemStack>>> SUPPLIERS = new ArrayList<>();

    /**
     * Use this method to add custom entry points to the watertank item stack supplier, e.g. getting them from custom
     * slots or items.
     */
    public static void addSupplier(Function<LivingEntity, List<ItemStack>> supplier) {
        SUPPLIERS.add(supplier);
    }

    public static float getWater(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return Math.min(tag.getFloat("Water"), maxWater(stack));
    }

    public static int maxWater(ItemStack stack) {
        var capacityLevel = Services.CREATE.getCapacityEnchantment().map(it ->
                EnchantmentHelper.getItemEnchantmentLevel(it, stack)
        ).orElse(0);

        return maxWater(capacityLevel);
    }

    public static int maxWater(int enchantLevel) {
        return Services.CONFIGS.server().waterInBacktank()
                + Services.CONFIGS.server().enchantedBacktankCapacity() * enchantLevel;
    }

    public static boolean hasWaterRemaining(ItemStack stack) {
        return getWater(stack) > 0;
    }

    public static Optional<ItemStack> get(LivingEntity entity) {
        var all = getAllWithWater(entity);
        if (all.isEmpty()) return Optional.empty();
        else return Optional.of(all.get(0));
    }

    public static List<ItemStack> getAllWithWater(LivingEntity entity) {
        List<ItemStack> all = new ArrayList<>();

        for (Function<LivingEntity, List<ItemStack>> supplier : SUPPLIERS) {
            List<ItemStack> result = supplier.apply(entity);

            for (ItemStack stack : result)
                if (hasWaterRemaining(stack))
                    all.add(stack);
        }

        // Sort with ascending order (we want to prioritize the most empty so things actually run out)
        all.sort((a, b) -> Float.compare(getWater(a), getWater(b)));

        return all;
    }

    public static void consumeWater(LivingEntity entity, ItemStack stack, float i) {
        CompoundTag tag = stack.getOrCreateTag();
        int maxAir = maxWater(stack);
        float air = getWater(stack);
        float newAir = Math.max(air - i, 0);
        tag.putFloat("Water", Math.min(newAir, maxAir));
        stack.setTag(tag);

        if (!(entity instanceof ServerPlayer player))
            return;

        Services.CREATE.sendWarning(player, air, newAir, maxAir / 10f);
        Services.CREATE.sendWarning(player, air, newAir, 1);
    }

}

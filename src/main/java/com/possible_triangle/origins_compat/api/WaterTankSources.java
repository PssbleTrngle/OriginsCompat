package com.possible_triangle.origins_compat.api;

import com.simibubi.create.content.equipment.armor.BacktankUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

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
        return BacktankUtil.getAir(stack);
    }

    public static boolean hasWaterRemaining(ItemStack stack) {
        return BacktankUtil.hasAirRemaining(stack);
    }

    public static Optional<ItemStack> get(LivingEntity entity) {
        var all = getAllWithWater(entity);
        if(all.isEmpty()) return Optional.empty();
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

}

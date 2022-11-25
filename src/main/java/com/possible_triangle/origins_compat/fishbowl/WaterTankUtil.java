package com.possible_triangle.origins_compat.fishbowl;

import com.possible_triangle.origins_compat.OriginsCompat;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.curiosities.armor.BackTankUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WaterTankUtil {

    public static ItemStack getWaterSource(LivingEntity entity) {
        for (var stack : entity.getArmorSlots()) {
            if (stack.is(CreateCompat.WATER_SOURCE)) return stack;
        }
        return ItemStack.EMPTY;
    }

    public static void onLivingTick(LivingEntity entity) {
        if (!OriginsCompat.requiresWater(entity)) return;

        Level world = entity.level;
        boolean drowning = entity.getAirSupply() == 0;

        if (world.isClientSide) {
            entity.getPersistentData().remove("VisualBacktankAir");
        }

        if (!AllItems.DIVING_HELMET.get().isWornBy(entity)) return;

        if (entity.isUnderWater()) return;
        if (entity instanceof Player player && player.isCreative()) return;

        var backtank = WaterTankUtil.getWaterSource(entity);
        if (backtank.isEmpty()) return;
        if (BackTankUtil.getAir(backtank) <= 0) return;

        if (drowning) entity.setAirSupply(10);

        if (world.isClientSide) {
            entity.getPersistentData().putInt("VisualBacktankAir", (int) BackTankUtil.getAir(backtank));
        }

        if (world.getGameTime() % 20 != 0) return;

        /* TODO
        if (entity instanceof ServerPlayer player) {
            AllAdvancements.DIVING_SUIT.awardTo(player);
        }
        */

        entity.setAirSupply(Math.min(entity.getMaxAirSupply(), entity.getAirSupply() + 10));
        entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
        BackTankUtil.consumeAir(entity, backtank, 1);
    }

}

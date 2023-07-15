package com.possible_triangle.origins_compat.compat.create;

import com.possible_triangle.origins_compat.OriginsCompat;
import com.possible_triangle.origins_compat.api.WaterTankSources;
import com.simibubi.create.content.equipment.armor.BacktankUtil;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WaterTankTicker {

    public static void onLivingTick(LivingEntity entity) {
        if (!OriginsCompat.requiresWater(entity)) return;

        Level world = entity.level;
        boolean drowning = entity.getAirSupply() == 0;

        if (world.isClientSide) {
            entity.getPersistentData().remove("VisualBacktankAir");
        }

        if (!DivingHelmetItem.isWornBy(entity)) return;

        if (entity.isUnderWater()) return;
        if (entity instanceof Player player && player.isCreative()) return;

        WaterTankSources.get(entity).ifPresent(backtank -> {
            if (drowning) entity.setAirSupply(10);

            if (world.isClientSide) {
                entity.getPersistentData().putInt("VisualBacktankAir", (int) WaterTankSources.getWater(backtank));
            }

            if (world.getGameTime() % 20 != 0) return;

            /* TODO
            if (entity instanceof ServerPlayer player) {
                AllAdvancements.DIVING_SUIT.awardTo(player);
            }
            */

            entity.setAirSupply(Math.min(entity.getMaxAirSupply(), entity.getAirSupply() + 10));
            entity.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, true, false, true));
            BacktankUtil.consumeAir(entity, backtank, 1);
        });
    }

}

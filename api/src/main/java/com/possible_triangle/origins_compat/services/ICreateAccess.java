package com.possible_triangle.origins_compat.services;

import com.possible_triangle.origins_compat.Services;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.Optional;

public interface ICreateAccess {

    default boolean isDisabled() {
        return !Services.PLATFORM.isModLoader("create");
    }

    Optional<Enchantment> getCapacityEnchantment();

    void sendWarning(ServerPlayer player, float oldValue, float newValue, float threshold);

    boolean isWearingDivingHelmet(LivingEntity entity);

    Item getWaterTankItem();

}

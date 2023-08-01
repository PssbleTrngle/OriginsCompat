package com.possible_triangle.origins_compat.services;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface IPlatformHelper {

    boolean isModLoaded(String id);

    CompoundTag persistentData(LivingEntity entity);

    boolean isFakePlayer(Player player);

}

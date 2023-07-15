package com.possible_triangle.origins_compat.services;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public interface IPlatformHelper {

    boolean isModLoader(String id);

    CompoundTag persistentData(LivingEntity entity);

}

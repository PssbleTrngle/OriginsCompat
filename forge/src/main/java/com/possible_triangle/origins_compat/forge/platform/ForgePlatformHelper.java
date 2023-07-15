package com.possible_triangle.origins_compat.forge.platform;

import com.possible_triangle.origins_compat.services.IPlatformHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoader(String id) {
        return ModList.get().isLoaded(id);
    }

    @Override
    public CompoundTag persistentData(LivingEntity entity) {
        return entity.getPersistentData();
    }

}

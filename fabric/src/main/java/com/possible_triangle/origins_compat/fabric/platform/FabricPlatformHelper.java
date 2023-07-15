package com.possible_triangle.origins_compat.fabric.platform;

import com.possible_triangle.origins_compat.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoader(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public CompoundTag persistentData(LivingEntity entity) {
        return entity.getExtraCustomData();
    }

}

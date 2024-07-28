package com.possible_triangle.origins_compat.fabric.platform;

import com.possible_triangle.origins_compat.services.IPlatformHelper;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean isModLoaded(String id) {
        return FabricLoader.getInstance().isModLoaded(id);
    }

    @Override
    public CompoundTag persistentData(LivingEntity entity) {
        return entity.getCustomData();
    }

    @Override
    public boolean isFakePlayer(Player player) {
        return player instanceof FakePlayer;
    }
}

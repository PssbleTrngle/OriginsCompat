package com.possible_triangle.origins_compat.forge.powers;

import com.mojang.serialization.Codec;
import com.possible_triangle.origins_compat.Constants;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraft.world.entity.Entity;

public class EmptyPowerType<T extends IDynamicFeatureConfiguration> extends PowerFactory<T> {

    private final String requiredMod;

    public EmptyPowerType(String requiredMod, Codec<T> codec) {
        super(codec);
        this.requiredMod = requiredMod;
    }

    @Override
    protected void onGained(T configuration, Entity entity) {
        var id  = ApoliRegistries.POWER_FACTORY.get().getKey(this);
        Constants.LOGGER.warn("Power '{}' requires mod '{}' to function", id, this.requiredMod);
    }
}

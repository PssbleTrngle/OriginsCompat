package com.possible_triangle.origins_compat.fabric.powers.config;

import net.minecraft.resources.ResourceLocation;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

public record ScalePowerConfig(ResourceLocation type, float size) {

    public ScaleType getType() {
        return ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, type);
    }
}
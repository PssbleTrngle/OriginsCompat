package com.possible_triangle.origins_compat.fabric.powers.config;

import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

public record HungerBarPowerConfig(
        ResourceLocation texture,
        int index,
        boolean replaceAppleSkinTooltip,
        boolean replaceNourishment
) {
    public static final int SIZE = 9;

    private ResourceLocation suffixTexture(String with) {
        var path = texture.getPath();
        var extension = FilenameUtils.getExtension(path);
        var name = FilenameUtils.removeExtension(path);
        return new ResourceLocation(texture.getNamespace(), name + with + extension);
    }

    public ResourceLocation nourishmentTexture() {
        return suffixTexture("_nourishment");
    }

    public ResourceLocation saturationTexture() {
        return suffixTexture("_saturation");
    }

}

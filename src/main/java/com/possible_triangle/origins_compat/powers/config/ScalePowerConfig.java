package com.possible_triangle.origins_compat.powers.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.resources.ResourceLocation;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.Optional;

public record ScalePowerConfig(Optional<String> type, float size) implements IDynamicFeatureConfiguration {
    public static Codec<ScalePowerConfig> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.optionalFieldOf("attribute").forGetter(ScalePowerConfig::type),
                    Codec.FLOAT.fieldOf("size").forGetter(ScalePowerConfig::size)
            ).apply(builder, ScalePowerConfig::new)
    );


    public ScaleType getType() {
        var id = new ResourceLocation(type.orElse("pehkui:base"));
        return ScaleRegistries.getEntry(ScaleRegistries.SCALE_TYPES, id);
    }
}
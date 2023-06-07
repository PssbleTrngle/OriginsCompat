package com.possible_triangle.origins_compat.powers.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record HungerBarPowerConfig(ResourceLocation texture, int index) implements IDynamicFeatureConfiguration {

    private static final int SIZE = 9;

    public static Codec<HungerBarPowerConfig> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.fieldOf("texture").forGetter(it -> it.texture.toString()),
                    Codec.INT.optionalFieldOf("index").forGetter(it -> Optional.of(it.index / SIZE))
            ).apply(builder, (texture, index) -> new HungerBarPowerConfig(new ResourceLocation(texture), index.map(it -> it * SIZE).orElse(0)))
    );

}

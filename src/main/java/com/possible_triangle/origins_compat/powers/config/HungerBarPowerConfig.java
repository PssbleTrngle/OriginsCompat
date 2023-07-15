package com.possible_triangle.origins_compat.powers.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.FilenameUtils;

import java.util.Optional;

public record HungerBarPowerConfig(
        ResourceLocation texture,
        int index,
        boolean replaceAppleSkinTooltip,
        boolean replaceNourishment
) implements IDynamicFeatureConfiguration {

    private static final int SIZE = 9;

    public static Codec<HungerBarPowerConfig> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    Codec.STRING.fieldOf("texture").forGetter(it -> it.texture.toString()),
                    Codec.INT.optionalFieldOf("index").forGetter(it -> Optional.of(it.index / SIZE)),
                    Codec.BOOL.optionalFieldOf("replaceAppleSkinTooltip").forGetter(it -> Optional.of(it.replaceAppleSkinTooltip)),
                    Codec.BOOL.optionalFieldOf("replaceNourishment").forGetter(it -> Optional.of(it.replaceNourishment))
            ).apply(builder, (texture, index, replaceAppleSkinTooltip, replaceNourishment) ->
                    new HungerBarPowerConfig(new ResourceLocation(texture), index.map(it -> it * SIZE).orElse(0), replaceAppleSkinTooltip.orElse(true), replaceNourishment.orElse(true))
            )
    );

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

    //private TextureOffsets createTextureOffsets() {
    //    var offets = new TooltipOverlayHandler.TextureOffsets();
    //    offets.containerNegativeHunger = 43;
    //    offets.containerExtraHunger = 133;
    //    offets.containerNormalHunger = 16;
    //    offets.containerPartialHunger = 124;
    //    offets.containerMissingHunger = 34;
    //    offets.shankMissingFull = 70;
    //    offets.shankMissingPartial = offets.shankMissingFull + 9;
    //    offets.shankFull = 52;
    //    offets.shankPartial = offets.shankFull + 9;
    //    return offets;
    //}

    //public TextureOffsets saturationOffsets = createTextureOffsets();

}

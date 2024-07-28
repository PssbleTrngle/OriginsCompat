package com.possible_triangle.origins_compat.forge.mixins;

import com.possible_triangle.origins_compat.forge.HungerOverlay;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.helpers.TextureHelper;

@Mixin(targets = "squeek.appleskin.client.TooltipOverlayHandler$FoodTooltipRenderer")
public class FoodTooltipRendererMixin {
    @Unique
    private static final HungerOverlay.Blitter originsCompat$blitter = HungerOverlay.createBlitter(HungerOverlay::appleSkinPower);

    @Redirect(
            method = "renderImage",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V")
    )
    private void overwriteUV(GuiGraphics instance, ResourceLocation defaultTexture, int x, int y, int zIndex, float u, float v, int height, int width, int textureHeight, int textureWidth) {
        if (defaultTexture != TextureHelper.MC_ICONS) {
            instance.blit(defaultTexture, x, y, zIndex, u, v, height, width, textureHeight, textureWidth);
            return;
        }

        var power = HungerOverlay.appleSkinPower();
        var texture = power.map(HungerBarPowerConfig::texture).orElse(defaultTexture);
        originsCompat$blitter.blit(instance, texture, x, y, zIndex, u, v, height, width);
    }

}
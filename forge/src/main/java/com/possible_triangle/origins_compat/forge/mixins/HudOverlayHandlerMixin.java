package com.possible_triangle.origins_compat.forge.mixins;

import com.possible_triangle.origins_compat.forge.HungerOverlay;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.client.HUDOverlayHandler;
import squeek.appleskin.helpers.TextureHelper;

@Mixin(value = HUDOverlayHandler.class)
public class HudOverlayHandlerMixin {

    @Unique
    private static final HungerOverlay.Blitter originsCompat$blitter = HungerOverlay.createBlitter(HungerOverlay::appleSkinPower);
    @Unique
    private static final HungerOverlay.Blitter originsCompat$saturationBlitter = HungerOverlay.createSaturationBlitter();

    @ModifyArg(
            method = "drawHungerOverlay(IILnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IIFZ)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V")
    )
    private static ResourceLocation modifyTexture(ResourceLocation defaultTexture) {
        var power = HungerOverlay.appleSkinPower();
        return power.map(HungerBarPowerConfig::texture).orElse(defaultTexture);
    }

    @Redirect(
            method = "drawHungerOverlay(IILnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IIFZ)V",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V")
    )
    private static void overwriteUV(GuiGraphics instance, ResourceLocation defaultTexture, int x, int y, int u, int v, int height, int width) {
        var power = HungerOverlay.appleSkinPower();
        var texture = power.map(HungerBarPowerConfig::texture).orElse(defaultTexture);
        originsCompat$blitter.blit(instance, texture, x, y, 0, u, v, height, width);
    }

    @Redirect(
            method = "drawSaturationOverlay(FFLnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V")
    )
    private static void overwriteSaturationUV(GuiGraphics instance, ResourceLocation defaultTexture, int x, int y, int u, int v, int height, int width) {
        var power = HungerOverlay.appleSkinPower();
        var texture =  power.map(config -> {
            if (defaultTexture == TextureHelper.MC_ICONS) return config.texture();
            else return config.saturationTexture();
        }).orElse(defaultTexture);
        originsCompat$saturationBlitter.blit(instance, texture, x, y, 0, u, v, height, width);
    }

}

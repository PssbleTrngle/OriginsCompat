package com.possible_triangle.origins_compat.forge.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.Constants;
import com.possible_triangle.origins_compat.forge.HungerOverlay;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.client.HUDOverlayHandler;

@Mixin(value = HUDOverlayHandler.class)
public class HudOverlayHandlerMixin {

    @Unique
    private static final HungerOverlay.Blitter originsCompat$blitter = HungerOverlay.createBlitter(HungerOverlay::appleSkinPower);
    @Unique
    private static final HungerOverlay.Blitter originsCompat$saturationBlitter = HungerOverlay.createSaturationBlitter();

    @ModifyArg(
            method = "drawHungerOverlay(IILnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIFZ)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
            index = 1
    )
    private static ResourceLocation modifyTexture(ResourceLocation defaultTexture) {
        var power = HungerOverlay.appleSkinPower();
        return power.map(HungerBarPowerConfig::texture).orElse(defaultTexture);
    }

    @ModifyArg(
            method = "drawSaturationOverlay(FFLnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
            index = 1
    )
    private static ResourceLocation modifySaturationTexture(ResourceLocation defaultTexture) {
        var power = HungerOverlay.appleSkinPower();
        return power.map(config -> {
            if (defaultTexture == GuiComponent.GUI_ICONS_LOCATION) return config.texture();
            else return config.saturationTexture();
        }).orElse(defaultTexture);
    }

    @Redirect(
            method = "drawHungerOverlay(IILnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIFZ)V",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V")
    )
    private static void overwriteUV(Gui instance, PoseStack poseStack, int x, int y, int u, int v, int height, int width) {
        originsCompat$blitter.blit(poseStack, x, y, instance.getBlitOffset(), u, v, height, width);
    }

    @Redirect(
            method = "drawSaturationOverlay(FFLnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V")
    )
    private static void overwriteSaturationUV(Gui instance, PoseStack poseStack, int x, int y, int u, int v, int height, int width) {
        originsCompat$saturationBlitter.blit(poseStack, x, y, instance.getBlitOffset(), u, v, height, width);
    }

}

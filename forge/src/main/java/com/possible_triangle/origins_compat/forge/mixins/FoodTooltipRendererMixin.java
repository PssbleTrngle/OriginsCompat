package com.possible_triangle.origins_compat.forge.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.forge.HungerOverlay;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.Optional;

@Mixin(targets = "squeek.appleskin.client.TooltipOverlayHandler$FoodTooltipRenderer")
public class FoodTooltipRendererMixin {
    @Unique
    private static final HungerOverlay.Blitter originsCompat$blitter = HungerOverlay.createBlitter(HungerOverlay::appleSkinPower);

    @ModifyArg(
            method = "renderImage",
            require = 0,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
            index = 1
    )
    private ResourceLocation modifyTexture(ResourceLocation defaultTexture) {
        var power = HungerOverlay.appleSkinPower();
        return power.flatMap(config -> {
            if (defaultTexture == GuiComponent.GUI_ICONS_LOCATION) return Optional.of(config.texture());
            else return Optional.empty();
        }).orElse(defaultTexture);
    }

    @Redirect(
            method = "renderImage",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiComponent;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIFFIIII)V"),
            slice = @Slice(
                    from = @At("HEAD"),
                    to = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V", ordinal = 1)
            )
    )
    private void overwriteUV(PoseStack poseStack, int x, int y, int zIndex, float u, float v, int height, int width, int textureHeight, int textureWidth) {
        originsCompat$blitter.blit(poseStack, x, y, zIndex, u, v, height, width);
    }

}
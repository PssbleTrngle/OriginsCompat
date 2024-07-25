package com.possible_triangle.origins_compat.forge.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.forge.HungerOverlay;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeGui.class)
public class HungerGuiMixin {

    @Unique
    private static final HungerOverlay.Blitter originsCompat$blitter = HungerOverlay.createBlitter(HungerOverlay::clientPower);

    @Redirect(method = "renderFood(IILcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V"))
    private void injectRender(ForgeGui instance, PoseStack poseStack, int x, int y, int textureX, int textureY, int height, int width) {
        HungerOverlay.appleSkinPower().ifPresent(config ->
                RenderSystem.setShaderTexture(0, config.texture())
        );
        originsCompat$blitter.blit(poseStack, x, y, instance.getBlitOffset(), textureX, textureY, height, width);
    }

}
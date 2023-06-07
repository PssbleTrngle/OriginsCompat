package com.possible_triangle.origins_compat.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.powers.HungerBarPowerType;
import net.minecraft.client.gui.GuiComponent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ForgeGui.class)
public class HungerGuiMixin {

    @Redirect(method = "renderFood", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/gui/overlay/ForgeGui;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIIII)V"))
    private void injectRender(ForgeGui instance, PoseStack poseStack, int x, int y, int textureX, int textureY, int height, int width) {
        var power = HungerBarPowerType.getPower();

        power.ifPresentOrElse(config -> {
            RenderSystem.setShaderTexture(0, config.texture());

            var newTextureX = textureX - 16;
            var newTextureY = textureY + config.index() - 27;
            GuiComponent.blit(poseStack, x, y, newTextureX, newTextureY, height, width, 126, 45);
        }, () -> {
            instance.blit(poseStack, x, y, textureX, textureY, height, width);
        });
    }

    //@Inject( method = "renderFood", at = @At(value = "HEAD"))
    //private void replaceTexture(int width, int height, PoseStack poseStack, CallbackInfo ci) {
    //    HungerBarPowerType.getPower().ifPresent(config ->
    //            RenderSystem.setShaderTexture(0, config.texture())
    //    );
    //}

}
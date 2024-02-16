package com.possible_triangle.origins_compat.forge.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.forge.powers.HungerBarPowerType;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(targets = "squeek.appleskin.client.TooltipOverlayHandler$FoodTooltipRenderer", remap = false)
public class FoodTooltipRendererMixin {

    @Unique
    private static Optional<HungerBarPowerConfig> originsCompat$clientPower() {
        var player = Minecraft.getInstance().player;
        return HungerBarPowerType.getPower(player).filter(HungerBarPowerConfig::replaceAppleSkinTooltip);
    }

    @ModifyArg(
            method = "renderImage",
            require = 0,
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
            index = 1
    )
    private ResourceLocation modifyTexture(ResourceLocation defaultTexture) {
        var power = originsCompat$clientPower();
        return power.map(config -> {
            if (defaultTexture == GuiComponent.GUI_ICONS_LOCATION) return config.texture();
            else return config.saturationTexture();
        }).orElse(defaultTexture);
    }

    //@ModifyVariable(method = "renderImage", require = 0, at = @At("STORE"))
    //private TooltipOverlayHandler.TextureOffsets modifyOffets(TooltipOverlayHandler.TextureOffsets defaultOffsets) {
    //    var power = HungerBarPowerType.getPower();
    //    return power.filter(HungerBarPowerConfig::replaceAppleSkinTooltip).map(config -> {
    //        if(defaultTexture == GuiComponent.GUI_ICONS_LOCATION) return config.texture();
    //        else return config.saturationTexture();
    //    }).orElse(defaultOffsets);
    //}

    //@Redirect(
    //        method = "renderImage",
    //        at = @At(value = "FIELD", target = "Lsqueek/appleskin/client/TooltipOverlayHandler$TextureOffsets;containerExtraHunger:I", opcode = Opcodes.GETFIELD)
    //)
    //private int injected(TooltipOverlayHandler.TextureOffsets instance) {
    //    return 0;
    //}

    @Redirect(
            method = "renderImage",
            require = 0,
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiComponent;blit(Lcom/mojang/blaze3d/vertex/PoseStack;IIIFFIIII)V")
    )
    private void overwriteSaturationUV(PoseStack poseStack, int offsetX, int offsetY, int zIndex, float textureX, float textureY, int height, int width, int textureHeight, int textureWidth) {
        originsCompat$clientPower().filter($ -> height == 9).ifPresentOrElse(config -> {
                    var newTextureX = textureX - 16;
                    var newTextureY = config.index();
                    GuiComponent.blit(poseStack, offsetX, offsetY, zIndex, newTextureX, newTextureY, 9, 9, 126, 45);
                }, () ->
                        GuiComponent.blit(poseStack, offsetX, offsetY, zIndex, textureX, textureY, height, width, textureHeight, textureWidth)
        );
    }

}
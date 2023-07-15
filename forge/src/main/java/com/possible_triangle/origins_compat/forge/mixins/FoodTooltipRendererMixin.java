package com.possible_triangle.origins_compat.mixins;

import com.possible_triangle.origins_compat.powers.HungerBarPowerType;
import com.possible_triangle.origins_compat.powers.config.HungerBarPowerConfig;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.client.TooltipOverlayHandler;

@Mixin(targets = "squeek.appleskin.client.TooltipOverlayHandler$FoodTooltipRenderer", remap = false)
public class FoodTooltipRendererMixin {

    @ModifyArg(method = "renderImage", require = 0, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"), index = 1)
    private ResourceLocation modifyTexture(ResourceLocation defaultTexture) {
        var power = HungerBarPowerType.getPower();
        return power.filter(HungerBarPowerConfig::replaceAppleSkinTooltip).map(config -> {
            if(defaultTexture == GuiComponent.GUI_ICONS_LOCATION) return config.texture();
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

    @Redirect(method = "renderImage", at = @At(value = "FIELD", target = "Lsqueek/appleskin/client/TooltipOverlayHandler$TextureOffsets;containerExtraHunger:I", opcode = Opcodes.GETFIELD))
    private int injected(TooltipOverlayHandler.TextureOffsets instance) {
        return 0;
    }

}
package com.possible_triangle.origins_compat.forge.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.possible_triangle.origins_compat.forge.HungerOverlay;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ForgeGui.class)
public class HungerGuiMixin {

    @Unique
    private static final HungerOverlay.Blitter originsCompat$blitter = HungerOverlay.createBlitter(HungerOverlay::clientPower);

    @WrapOperation(method = "renderFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V"))
    private void injectRender(GuiGraphics instance, ResourceLocation defaultTexture, int x, int y, int u, int v, int height, int width, Operation<Void> original) {
        var texture = HungerOverlay.appleSkinPower().map(HungerBarPowerConfig::texture).orElse(defaultTexture);
        originsCompat$blitter.blit(instance, texture, x, y, 0, u, v, height, width);
    }

}
package com.possible_triangle.origins_compat.fabric.mixins;

import com.possible_triangle.origins_compat.Services;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetMixin {

    @Inject(at = @At("HEAD"), cancellable = true, require = 0, method = "breatheUnderwater")
    private static void stealAir(LivingEntity entity, CallbackInfo ci) {
        if (Services.ORIGINS.requiresWater(entity)) {
            ci.cancel();
        }
    }

}

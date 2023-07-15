package com.possible_triangle.origins_compat.forge.mixins;

import com.possible_triangle.origins_compat.forge.ForgeEntrypoint;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetMixin {

    @Inject(at = @At("HEAD"), cancellable = true, require = 0, method = "breatheUnderwater")
    private static void stealAir(LivingEvent.LivingTickEvent event, CallbackInfo ci) {
        var entity = event.getEntity();
        if (ForgeEntrypoint.requiresWater(entity)) {
            ci.cancel();
        }
    }

}

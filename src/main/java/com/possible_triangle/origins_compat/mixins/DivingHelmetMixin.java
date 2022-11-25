package com.possible_triangle.origins_compat.mixins;

import com.possible_triangle.origins_compat.OriginsCompat;
import com.simibubi.create.content.curiosities.armor.DivingHelmetItem;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DivingHelmetItem.class, remap = false)
public class DivingHelmetMixin {

    @Inject(at = @At("HEAD"), cancellable = true, require = 0, method = "breatheUnderwater")
    private static void stealAir(LivingEvent.LivingUpdateEvent event, CallbackInfo ci) {
        var entity = event.getEntityLiving();
        if (OriginsCompat.requiresWater(entity)) {
            ci.cancel();
        }
    }

}

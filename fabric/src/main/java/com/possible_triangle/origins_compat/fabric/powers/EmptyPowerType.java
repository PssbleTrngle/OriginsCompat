package com.possible_triangle.origins_compat.fabric.powers;

import com.possible_triangle.origins_compat.Constants;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class EmptyPowerType extends Power {

    private final String requiredMod;
    private final ResourceLocation id;

    public EmptyPowerType(PowerType<?> type, LivingEntity entity, String requiredMod, ResourceLocation id) {
        super(type, entity);
        this.requiredMod = requiredMod;
        this.id = id;
    }

    @Override
    public void onGained() {
        Constants.LOGGER.warn("Power '{}' requires mod '{}' to function", id, this.requiredMod);
    }

    public static PowerFactory<?> createFactory(ResourceLocation id, String requiredMod) {
        return Power.createSimpleFactory((type, entity) -> new EmptyPowerType(type, entity, requiredMod, id), id);
    }
}

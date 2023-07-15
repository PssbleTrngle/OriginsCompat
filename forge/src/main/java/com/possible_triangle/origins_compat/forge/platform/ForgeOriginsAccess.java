package com.possible_triangle.origins_compat.forge.platform;

import com.possible_triangle.origins_compat.services.IOriginsAccess;
import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import net.minecraft.world.entity.LivingEntity;

public class ForgeOriginsAccess implements IOriginsAccess {

    @Override
    public boolean requiresWater(LivingEntity entity) {
        return IPowerContainer.hasPower(entity, OriginsPowerTypes.WATER_BREATHING.get());
    }

}

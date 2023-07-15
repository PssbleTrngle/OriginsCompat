package com.possible_triangle.origins_compat.fabric.platform;

import com.possible_triangle.origins_compat.services.IOriginsAccess;
import net.minecraft.world.entity.LivingEntity;

public class FabricOriginsAccess implements IOriginsAccess {

    @Override
    public boolean requiresWater(LivingEntity entity) {
        return  false;
        //return IPowerContainer.hasPower(entity, OriginsPowerTypes.WATER_BREATHING.get());
    }

}

package com.possible_triangle.origins_compat.powers;

import com.possible_triangle.origins_compat.powers.config.ScalePowerConfig;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.world.entity.Entity;
import virtuoel.pehkui.api.ScaleData;

public class ScalePowerType extends PowerFactory<ScalePowerConfig> {

    public ScalePowerType() {
        super(ScalePowerConfig.CODEC);
    }

    private ScaleData scaleData(ScalePowerConfig configuration, Entity entity) {
        var type = configuration.getType();
        return type.getScaleData(entity);
    }

    @Override
    protected void onGained(ScalePowerConfig configuration, Entity entity) {
        super.onGained(configuration, entity);
        var data = scaleData(configuration, entity);
        data.setPersistence(true);
        data.setTargetScale(configuration.size());
    }

    @Override
    protected void onLost(ScalePowerConfig configuration, Entity entity) {
        super.onLost(configuration, entity);
        scaleData(configuration, entity).resetScale();
    }

    @Override
    protected void onRespawn(ScalePowerConfig configuration, Entity entity) {
        super.onRespawn(configuration, entity);
        scaleData(configuration, entity).setScale(configuration.size());
    }
}

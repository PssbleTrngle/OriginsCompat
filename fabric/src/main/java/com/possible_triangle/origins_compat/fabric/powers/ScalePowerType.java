package com.possible_triangle.origins_compat.fabric.powers;

import com.possible_triangle.origins_compat.fabric.powers.config.HungerBarPowerConfig;
import com.possible_triangle.origins_compat.fabric.powers.config.ScalePowerConfig;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import virtuoel.pehkui.api.ScaleData;

public class ScalePowerType extends Power {

    public final ScalePowerConfig config;

    public ScalePowerType(PowerType<?> type, LivingEntity entity, ScalePowerConfig config) {
        super(type, entity);
        this.config = config;
    }

    private ScaleData scaleData() {
        var type = config.getType();
        return type.getScaleData(entity);
    }

    @Override
    public void onGained() {
        super.onGained();
        var data = scaleData();
        data.setPersistence(true);
        data.setTargetScale(config.size());
    }

    @Override
    public void onLost() {
        super.onLost();
        scaleData().resetScale();
    }

    @Override
    public void onRespawn() {
        super.onRespawn();
        scaleData().setScale(config.size());
    }

    public static PowerFactory<ScalePowerType> createFactory(ResourceLocation id) {
        return new PowerFactory<ScalePowerType>(id,
                new SerializableData()
                        .add("attribute", SerializableDataTypes.IDENTIFIER, new ResourceLocation("pehkui:base"))
                        .add("size", SerializableDataTypes.FLOAT, null),
                data ->
                        (type, player) ->
                                new ScalePowerType(type, player,
                                        new ScalePowerConfig(
                                                data.getId("attribute"),
                                                data.getFloat("size")
                                        )
                                )
        ).allowCondition();
    }
}

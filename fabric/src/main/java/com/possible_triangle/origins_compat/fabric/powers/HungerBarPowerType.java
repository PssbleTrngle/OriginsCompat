package com.possible_triangle.origins_compat.fabric.powers;

import com.possible_triangle.origins_compat.fabric.powers.config.HungerBarPowerConfig;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class HungerBarPowerType extends Power {

    public final HungerBarPowerConfig config;

    public HungerBarPowerType(PowerType<?> type, LivingEntity entity, HungerBarPowerConfig config) {
        super(type, entity);
        this.config = config;
    }

    public static Optional<HungerBarPowerConfig> getPower(Entity entity) {
        return PowerHolderComponent.getPowers(entity, HungerBarPowerType.class).stream()
                .map(it -> it.config)
                .findFirst();
    }

    public static PowerFactory<HungerBarPowerType> createFactory(ResourceLocation id) {
        return new PowerFactory<HungerBarPowerType>(id,
                new SerializableData()
                        .add("texture", SerializableDataTypes.IDENTIFIER, null)
                        .add("index", SerializableDataTypes.INT, 0)
                        .add("replaceAppleSkinTooltip", SerializableDataTypes.BOOLEAN, true)
                        .add("replaceNourishment", SerializableDataTypes.BOOLEAN, true),
                data ->
                        (type, player) ->
                                new HungerBarPowerType(type, player,
                                        new HungerBarPowerConfig(
                                                data.getId("texture"),
                                                data.getInt("index") * HungerBarPowerConfig.SIZE,
                                                data.getBoolean("replaceAppleSkinTooltip"),
                                                data.getBoolean("replaceNourishment")
                                        )
                                )
        ).allowCondition();
    }


}

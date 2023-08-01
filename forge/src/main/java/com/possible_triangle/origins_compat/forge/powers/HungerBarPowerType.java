package com.possible_triangle.origins_compat.forge.powers;

import com.possible_triangle.origins_compat.forge.ForgeEntrypoint;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import io.github.edwinmindcraft.apoli.api.component.IPowerContainer;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public class HungerBarPowerType extends PowerFactory<HungerBarPowerConfig> {

    public HungerBarPowerType() {
        super(HungerBarPowerConfig.CODEC);
    }

    public static Optional<HungerBarPowerConfig> getPower(Entity entity) {
        return ForgeEntrypoint.HUNGER_BAR
                .flatMap(power -> IPowerContainer.getPowers(entity, power)
                        .stream()
                        .map(Holder::get)
                        .map(ConfiguredPower::getConfiguration)
                        .findFirst());
    }

}

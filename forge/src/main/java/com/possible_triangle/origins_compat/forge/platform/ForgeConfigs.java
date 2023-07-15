package com.possible_triangle.origins_compat.forge.platform;

import com.possible_triangle.origins_compat.services.IConfigs;
import com.simibubi.create.infrastructure.config.AllConfigs;

public class ForgeConfigs implements IConfigs {

    private final IConfigs.Server server = new Server() {
        @Override
        public int waterInBacktank() {
            return AllConfigs.server().equipment.airInBacktank.get();
        }

        @Override
        public int enchantedBacktankCapacity() {
            return AllConfigs.server().equipment.enchantedBacktankCapacity.get();
        }
    };

    @Override
    public Server server() {
        return server;
    }
}

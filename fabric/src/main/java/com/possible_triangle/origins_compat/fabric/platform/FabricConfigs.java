package com.possible_triangle.origins_compat.fabric.platform;

import com.possible_triangle.origins_compat.services.IConfigs;
import com.simibubi.create.infrastructure.config.AllConfigs;

public class FabricConfigs implements IConfigs {

    private final Server server = new Server() {
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

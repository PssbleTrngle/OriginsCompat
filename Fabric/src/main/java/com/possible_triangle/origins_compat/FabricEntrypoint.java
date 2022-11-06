package com.possible_triangle.origins_compat;

import net.fabricmc.api.ModInitializer;

public class FabricEntrypoint implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init();
    }
}

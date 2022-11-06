package com.possible_triangle.origins_compat;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class ForgeEntrypoint {
    
    public ForgeEntrypoint() {
        CommonClass.init();
    }
}
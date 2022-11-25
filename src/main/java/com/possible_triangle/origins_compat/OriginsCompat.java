package com.possible_triangle.origins_compat;

import com.possible_triangle.origins_compat.powers.ScalePowerType;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(OriginsCompat.MOD_ID)
public class OriginsCompat {

    public static final String MOD_ID = "origins_compat";
    public static final String MOD_NAME = "Origins Compat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final DeferredRegister<PowerFactory<?>> POWERS = DeferredRegister.create(ApoliRegistries.POWER_FACTORY_KEY, MOD_ID);

    public static final Object SCALE_POWER = POWERS.register("scale", ScalePowerType::new);
    
    public OriginsCompat() {
        POWERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
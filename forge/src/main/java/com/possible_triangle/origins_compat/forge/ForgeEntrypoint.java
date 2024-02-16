package com.possible_triangle.origins_compat.forge;

import com.mojang.serialization.Codec;
import com.possible_triangle.origins_compat.CommonClass;
import com.possible_triangle.origins_compat.Services;
import com.possible_triangle.origins_compat.forge.powers.EmptyPowerType;
import com.possible_triangle.origins_compat.forge.powers.HungerBarPowerType;
import com.possible_triangle.origins_compat.forge.powers.ScalePowerType;
import com.possible_triangle.origins_compat.forge.powers.config.ScalePowerConfig;
import io.github.edwinmindcraft.apoli.api.IDynamicFeatureConfiguration;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.possible_triangle.origins_compat.Constants.MOD_ID;

@Mod(MOD_ID)
public class ForgeEntrypoint {

    public static final DeferredRegister<PowerFactory<?>> POWERS = DeferredRegister.create(ApoliRegistries.POWER_FACTORY_KEY, MOD_ID);

    public static final RegistryObject<HungerBarPowerType> HUNGER_BAR = POWERS.register("hunger_bar", HungerBarPowerType::new);

    static {
        POWERS.register("scale", optionalPower("pehkui", ScalePowerType::new, ScalePowerConfig.CODEC));
    }

    public ForgeEntrypoint() {
        POWERS.register(FMLJavaModLoadingContext.get().getModEventBus());

        CommonClass.init();

        if(Services.PLATFORM.isModLoaded("create")) {
            ForgeCreateCompat.init();
        }
    }

    public static <T extends IDynamicFeatureConfiguration> Supplier<PowerFactory<T>> optionalPower(String requiredMod, Function<Codec<T>, PowerFactory<T>> supplier, Codec<T> codec) {
        return optionalEntry(requiredMod, () -> supplier.apply(codec), () -> new EmptyPowerType<>(requiredMod, codec));
    }

    public static <T> Supplier<T> optionalEntry(String requiredMod, Supplier<T> supplier, Supplier<T> empty) {
        if (Services.PLATFORM.isModLoaded(requiredMod)) return supplier;
        return empty;
    }
}
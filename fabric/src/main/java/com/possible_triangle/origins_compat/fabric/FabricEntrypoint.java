package com.possible_triangle.origins_compat.fabric;

import com.possible_triangle.origins_compat.CommonClass;
import com.possible_triangle.origins_compat.Services;
import com.possible_triangle.origins_compat.fabric.powers.EmptyPowerType;
import com.possible_triangle.origins_compat.fabric.powers.HungerBarPowerType;
import com.possible_triangle.origins_compat.fabric.powers.ScalePowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

import static com.possible_triangle.origins_compat.Constants.MOD_ID;

public class FabricEntrypoint implements ModInitializer, ClientModInitializer {

    private static void registerPower(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void registerPower(PowerFactorySupplier<?> factorySupplier) {
        registerPower(factorySupplier.createFactory());
    }

    private static PowerFactory<?> optionalPower(String requiredMod, Function<ResourceLocation, PowerFactory<?>> supplier, ResourceLocation id) {
        if (Services.PLATFORM.isModLoaded(requiredMod)) return supplier.apply(id);
        return EmptyPowerType.createFactory(id, requiredMod);
    }

    @Override
    public void onInitialize() {
        CommonClass.init();

        registerPower(HungerBarPowerType.createFactory(new ResourceLocation(MOD_ID, "hunger_bar")));
        registerPower(optionalPower("pekhui", ScalePowerType::createFactory, new ResourceLocation(MOD_ID, "scale")));

        if (Services.PLATFORM.isModLoaded("create")) {
            FabricCreateCompat.init();
        }
    }

    @Override
    public void onInitializeClient() {
        if (Services.PLATFORM.isModLoaded("create")) {
            FabricCreateCompat.registerOverlays();
        }
    }

}

package com.possible_triangle.origins_compat.forge;

import com.possible_triangle.origins_compat.CommonCreateCompat;
import com.possible_triangle.origins_compat.client.WaterTankOverlay;
import com.possible_triangle.origins_compat.forge.logic.WaterTankSpoutBehaviour;
import com.possible_triangle.origins_compat.logic.WaterTankTicker;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static com.possible_triangle.origins_compat.Constants.MOD_ID;

public class ForgeCreateCompat {

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID)
            .setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);

    public static void init() {
        WaterTankSpoutBehaviour.register();

        var forgeBus = MinecraftForge.EVENT_BUS;
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonCreateCompat.register(REGISTRATE);
        REGISTRATE.registerEventListeners(modBus);

        forgeBus.addListener(ForgeCreateCompat::onLivingUpdate);
        modBus.addListener(ForgeCreateCompat::registerOverlays);
    }

    public static void onLivingUpdate(LivingEvent.LivingTickEvent event) {
        WaterTankTicker.onLivingTick(event.getEntity());
    }

    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.AIR_LEVEL.id(), "remaining_water", (gui, graphics, partialTick, width, height) -> {
            WaterTankOverlay.renderRemainingWaterOverlay(graphics, width, height);
        });
    }

}

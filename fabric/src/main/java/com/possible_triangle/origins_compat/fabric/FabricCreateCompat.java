package com.possible_triangle.origins_compat.fabric;

import com.possible_triangle.origins_compat.CommonCreateCompat;
import com.possible_triangle.origins_compat.client.WaterTankOverlay;
import com.possible_triangle.origins_compat.fabric.logic.WaterTankSpoutBehaviour;
import com.possible_triangle.origins_compat.logic.WaterTankTicker;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.fabricators_of_create.porting_lib.event.client.OverlayRenderCallback;
import io.github.fabricators_of_create.porting_lib.event.common.PlayerTickEvents;

import static com.possible_triangle.origins_compat.Constants.MOD_ID;

public class FabricCreateCompat {

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public static void init() {
        WaterTankSpoutBehaviour.register();

        CommonCreateCompat.register(REGISTRATE);
        REGISTRATE.register();

        PlayerTickEvents.START.register(WaterTankTicker::onLivingTick);
    }

    public static void registerOverlays() {
        OverlayRenderCallback.EVENT.register(((stack, partialTicks, window, type) -> {
            if (type == OverlayRenderCallback.Types.AIR) {
                WaterTankOverlay.renderRemainingWaterOverlay(stack, window.getGuiScaledWidth(), window.getGuiScaledHeight());
            }
            return false;
        }));
    }

}

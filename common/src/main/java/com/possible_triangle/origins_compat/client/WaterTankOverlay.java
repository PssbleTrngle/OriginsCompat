package com.possible_triangle.origins_compat.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.possible_triangle.origins_compat.CommonCreateCompat;
import com.possible_triangle.origins_compat.Services;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.GameType;

public class WaterTankOverlay {

    public static void renderRemainingWaterOverlay(PoseStack poseStack, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.gameMode == null) return;
        if (mc.options.hideGui || mc.gameMode.getPlayerMode() == GameType.SPECTATOR) return;

        LocalPlayer player = mc.player;
        if (player == null) return;
        if (player.isCreative()) return;
        var persistentData = Services.PLATFORM.persistentData(player);
        if (!persistentData.contains("VisualBacktankAir")) return;
        if (!Services.ORIGINS.requiresWater(player)) return;
        if (player.isUnderWater()) return;

        int timeLeft = persistentData.getInt("VisualBacktankAir");

        poseStack.pushPose();

        poseStack.translate(width / 2 + 90, height - 53, 0);

        var text = Component.literal(StringUtil.formatTickDuration(timeLeft * 20));
        GuiGameElement.of(CommonCreateCompat.getWaterBacktankItem()).at(0, 0).render(poseStack);

        int color = 0xFF_FFFFFF;

        if (timeLeft < 60 && timeLeft % 2 == 0) {
            color = Color.mixColors(0xFF_FF0000, color, Math.max(timeLeft / 60f, .25f));
        }

        Minecraft.getInstance().font.drawShadow(poseStack, text, 16, 5, color);

        poseStack.popPose();
    }

}

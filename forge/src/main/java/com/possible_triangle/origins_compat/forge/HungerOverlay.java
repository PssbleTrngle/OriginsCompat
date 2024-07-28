package com.possible_triangle.origins_compat.forge;

import com.possible_triangle.origins_compat.forge.powers.HungerBarPowerType;
import com.possible_triangle.origins_compat.forge.powers.config.HungerBarPowerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public class HungerOverlay {

    public static Optional<HungerBarPowerConfig> clientPower() {
        var player = Minecraft.getInstance().player;
        return HungerBarPowerType.getPower(player).filter(HungerBarPowerConfig::replaceAppleSkinTooltip);
    }

    public static Optional<HungerBarPowerConfig> appleSkinPower() {
        return clientPower().filter(HungerBarPowerConfig::replaceAppleSkinTooltip);
    }

    public static Blitter createBlitter(Supplier<Optional<HungerBarPowerConfig>> configSupplier) {
        return (graphics, texture, x, y, zIndex, u, v, height, width) -> {
            configSupplier.get().ifPresentOrElse(config -> {
                graphics.blit(texture, x, y, zIndex, u - 16, config.index(), height, width, 126, 45);
            }, () -> {
                graphics.blit(texture, x, y, zIndex, u, v, height, width, 256, 256);
            });
        };
    }

    public static Blitter createSaturationBlitter() {
        return (graphics, texture, x, y, zIndex, u, v, height, width) -> {
            appleSkinPower().ifPresentOrElse(config -> {
                graphics.blit(texture, x, y, zIndex, u, config.index(), height, width, 126, 45);
            }, () -> {
                graphics.blit(texture, x, y, zIndex, u, v, height, width, 256, 256);
            });
        };
    }

    @FunctionalInterface
    public interface Blitter {
        void blit(GuiGraphics graphics, ResourceLocation texture, int x, int y, int zIndex, float u, float v, int height, int width);
    }

}

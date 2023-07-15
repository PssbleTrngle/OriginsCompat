package com.possible_triangle.origins_compat.forge.platform;

import com.possible_triangle.origins_compat.forge.compat.create.CreateCompat;
import com.possible_triangle.origins_compat.services.ICreateAccess;
import com.simibubi.create.AllEnchantments;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

public class ForgeCreateAccess implements ICreateAccess {

    @Override
    public Optional<Enchantment> getCapacityEnchantment() {
        if (isDisabled()) return Optional.empty();
        return Optional.of(AllEnchantments.CAPACITY.get());
    }

    @Override
    public void sendWarning(ServerPlayer player, float oldValue, float newValue, float threshold) {
        if (isDisabled()) return;

        if (newValue > threshold)
            return;
        if (oldValue <= threshold)
            return;

        boolean depleted = threshold == 1;
        MutableComponent component = Lang.translateDirect(depleted ? "backtank.depleted" : "backtank.low");

        AllSoundEvents.DENY.play(player.level, null, player.blockPosition(), 1, 1.25f);
        AllSoundEvents.STEAM.play(player.level, null, player.blockPosition(), .5f, .5f);

        player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 40, 10));
        player.connection.send(new ClientboundSetSubtitleTextPacket(
                Components.literal("\u26A0 ").withStyle(depleted ? ChatFormatting.RED : ChatFormatting.GOLD)
                        .append(component.withStyle(ChatFormatting.GRAY))));
        player.connection.send(new ClientboundSetTitleTextPacket(Components.immutableEmpty()));
    }

    @Override
    public boolean isWearingDivingHelmet(LivingEntity entity) {
        return DivingHelmetItem.isWornBy(entity);
    }

    @Override
    public Item getWaterTankItem() {
        return CreateCompat.WATER_BACKTANK_ITEM.get();
    }
}

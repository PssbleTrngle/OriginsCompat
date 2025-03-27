package com.possible_triangle.origins_compat.forge.platform;

import com.possible_triangle.origins_compat.forge.logic.WaterTankSpoutBehaviour;
import com.possible_triangle.origins_compat.services.ICreateAccess;
import com.simibubi.create.AllEnchantments;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.equipment.armor.DivingHelmetItem;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntityType;

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
        MutableComponent component = CreateLang.translateDirect(depleted ? "backtank.depleted" : "backtank.low");

        AllSoundEvents.DENY.play(player.level(), null, player.blockPosition(), 1, 1.25f);
        AllSoundEvents.STEAM.play(player.level(), null, player.blockPosition(), .5f, .5f);

        player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 40, 10));
        player.connection.send(new ClientboundSetSubtitleTextPacket(
                Component.literal("\u26A0 ").withStyle(depleted ? ChatFormatting.RED : ChatFormatting.GOLD)
                        .append(component.withStyle(ChatFormatting.GRAY))));
        player.connection.send(new ClientboundSetTitleTextPacket(CommonComponents.EMPTY));
    }

    @Override
    public boolean isWearingDivingHelmet(LivingEntity entity) {
        return DivingHelmetItem.isWornBy(entity);
    }

    @Override
    public void registerSpoutBehaviour(BlockEntityType<?> blockEntity) {
        BlockSpoutingBehaviour.BY_BLOCK_ENTITY.register(blockEntity, new WaterTankSpoutBehaviour());
    }

}

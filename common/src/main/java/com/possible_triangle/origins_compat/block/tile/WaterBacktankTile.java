package com.possible_triangle.origins_compat.block.tile;

import com.possible_triangle.origins_compat.CommonCreateCompat;
import com.possible_triangle.origins_compat.Constants;
import com.possible_triangle.origins_compat.Services;
import com.possible_triangle.origins_compat.api.WaterTankSources;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class WaterBacktankTile extends SmartBlockEntity implements Nameable {

    private int capacityEnchantLevel;
    private int waterLevel;
    private int timer;
    private @Nullable Component customName;
    private ListTag enchantmentTag = new ListTag();

    public static final ResourceLocation SPOUT_BEHAVIOUR = new ResourceLocation(Constants.MOD_ID, "water_backtank");

    public WaterBacktankTile(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    public int getComparatorOutput() {
        int max = WaterTankSources.maxWater(capacityEnchantLevel);
        return ComparatorUtil.fractionToRedstoneLevel(waterLevel / (float) max);
    }

    public void setCapacityEnchantLevel(int capacityEnchantLevel) {
        this.capacityEnchantLevel = capacityEnchantLevel;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Water", waterLevel);
        compound.putInt("Timer", timer);
        compound.putInt("CapacityEnchantment", capacityEnchantLevel);
        if (customName != null)
            compound.putString("CustomName", Component.Serializer.toJson(customName));
        compound.put("Enchantments", enchantmentTag);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        int prev = waterLevel;
        capacityEnchantLevel = compound.getInt("CapacityEnchantment");
        waterLevel = compound.getInt("Water");
        timer = compound.getInt("Timer");
        enchantmentTag = compound.getList("Enchantments", Tag.TAG_COMPOUND);
        if (compound.contains("CustomName", 8))
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        if (prev != 0 && prev != waterLevel && waterLevel == WaterTankSources.maxWater(capacityEnchantLevel) && clientPacket)
            playFilledEffect();
    }

    protected void playFilledEffect() {
        AllSoundEvents.CONFIRM.playAt(level, worldPosition, 0.4f, 1, true);
        Vec3 baseMotion = new Vec3(.25, 0.1, 0);
        Vec3 baseVec = VecHelper.getCenterOf(worldPosition);
        for (int i = 0; i < 360; i += 10) {
            Vec3 m = VecHelper.rotate(baseMotion, i, Direction.Axis.Y);
            Vec3 v = baseVec.add(m.normalize()
                    .scale(.25f));

            level.addParticle(ParticleTypes.SPIT, v.x, v.y, v.z, m.x, m.y, m.z);
        }
    }

    @Override
    public Component getName() {
        return Optional.ofNullable(customName).orElseGet(() -> CommonCreateCompat.getWaterBacktankItem().getDescription());
    }

    public int getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this.waterLevel = waterLevel;
        sendData();
    }

    public void setCustomName(@Nullable Component customName) {
        this.customName = customName;
    }

    @Override
    public Component getCustomName() {
        return customName;
    }

    public ListTag getEnchantmentTag() {
        return enchantmentTag;
    }

    public void setEnchantmentTag(ListTag enchantmentTag) {
        this.enchantmentTag = enchantmentTag;
    }

    public static int fillBySpout(Level world, BlockPos pos, Fluid fluid, int availableAmount, boolean simulate) {
        if (!fluid.is(FluidTags.WATER)) return 0;

        var perFill = Math.min(100, availableAmount);

        var tile = world.getBlockEntity(pos);
        if (tile instanceof WaterBacktankTile tank) {
            int max = WaterTankSources.maxWater(tank.capacityEnchantLevel);
            var amountFilled = Math.min(max - tank.waterLevel, perFill);
            if (!simulate) tank.setWaterLevel(tank.waterLevel + amountFilled);
            return amountFilled;
        } else {
            return 0;
        }
    }

}

package com.possible_triangle.origins_compat.forge.logic;

import com.possible_triangle.origins_compat.block.tile.WaterBacktankTile;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class WaterTankSpoutBehaviour implements BlockSpoutingBehaviour {

    @Override
    public int fillBlock(Level world, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        return WaterBacktankTile.fillBySpout(world, pos, availableFluid.getFluid(), availableFluid.getAmount(), simulate);
    }

}

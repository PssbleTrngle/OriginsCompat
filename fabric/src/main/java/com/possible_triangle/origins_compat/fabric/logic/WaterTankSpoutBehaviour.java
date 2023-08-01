package com.possible_triangle.origins_compat.fabric.logic;

import com.possible_triangle.origins_compat.block.tile.WaterBacktankTile;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class WaterTankSpoutBehaviour extends BlockSpoutingBehaviour {

    public static void register() {
        BlockSpoutingBehaviour.addCustomSpoutInteraction(WaterBacktankTile.SPOUT_BEHAVIOUR, new WaterTankSpoutBehaviour());
    }

    @Override
    public long fillBlock(Level world, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        var amount = availableFluid.getAmount() / FluidConstants.BUCKET * 1000;
        return WaterBacktankTile.fillBySpout(world, pos, availableFluid.getFluid(), (int) amount, simulate);
    }

}

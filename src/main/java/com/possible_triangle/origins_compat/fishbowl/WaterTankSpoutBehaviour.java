package com.possible_triangle.origins_compat.fishbowl;

import com.possible_triangle.origins_compat.fishbowl.tile.WaterBacktankTile;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.content.contraptions.fluids.actors.SpoutTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class WaterTankSpoutBehaviour extends BlockSpoutingBehaviour {

    public static void register() {
        BlockSpoutingBehaviour.addCustomSpoutInteraction(WaterBacktankTile.SPOUT_BEHAVIOUR, new WaterTankSpoutBehaviour());
    }

    @Override
    public int fillBlock(Level world, BlockPos pos, SpoutTileEntity spout, FluidStack availableFluid, boolean simulate) {
        return WaterBacktankTile.fillBySpout(world, pos, availableFluid.getFluid(), availableFluid.getAmount(), simulate);
    }

}

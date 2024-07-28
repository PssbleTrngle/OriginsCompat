package com.possible_triangle.origins_compat.block;

import com.possible_triangle.origins_compat.CommonCreateCompat;
import com.possible_triangle.origins_compat.Services;
import com.possible_triangle.origins_compat.block.tile.WaterBacktankTile;
import com.simibubi.create.AllEnchantments;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class WaterBacktank extends Block implements IBE<WaterBacktankTile>, SimpleWaterloggedBlock, IWrenchable {

    private static final VoxelShape SHAPE = box(3, 0, 3, 13, 12, 13);

    public WaterBacktank(Properties properties) {
        super(properties);
    }


    @SuppressWarnings("unchecked")
    @Override
    public BlockEntityType<WaterBacktankTile> getBlockEntityType() {
        return CommonCreateCompat.getWaterBacktankBlockEntity();
    }

    @Override
    public Class<WaterBacktankTile> getBlockEntityClass() {
        return WaterBacktankTile.class;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, HORIZONTAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public int getAnalogOutputSignal(BlockState p_180641_1_, Level world, BlockPos pos) {
        return getBlockEntityOptional(world, pos).map(WaterBacktankTile::getComparatorOutput).orElse(0);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED))
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return super.getStateForPlacement(context)
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        if (stack == null || stack.isEmpty()) return;
        if (world.isClientSide) return;
        withBlockEntityDo(world, pos, te -> {
            te.setCapacityEnchantLevel(EnchantmentHelper.getItemEnchantmentLevel(AllEnchantments.CAPACITY.get(), stack));
            te.setWaterLevel(stack.getOrCreateTag().getInt("Water"));
            if (stack.isEnchanted()) te.setEnchantmentTag(stack.getEnchantmentTags());
            if (stack.hasCustomHoverName()) te.setCustomName(stack.getHoverName());
        });
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player == null) return InteractionResult.PASS;
        if (Services.PLATFORM.isFakePlayer(player)) return InteractionResult.PASS;
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (player.getMainHandItem().getItem() instanceof BlockItem) return InteractionResult.PASS;
        if (!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) return InteractionResult.PASS;
        if (!world.isClientSide) {
            world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, .75f, 1);
            player.setItemSlot(EquipmentSlot.CHEST, getCloneItemStack(world, pos, state));
            world.destroyBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        var stack = new ItemStack(CommonCreateCompat.getWaterBacktankItem());
        var tile = getBlockEntityOptional(world, pos);

        int water = tile.map(WaterBacktankTile::getWaterLevel).orElse(0);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Water", water);

        ListTag enchants = tile.map(WaterBacktankTile::getEnchantmentTag)
                .orElse(new ListTag());
        if (!enchants.isEmpty()) {
            ListTag enchantmentTagList = stack.getEnchantmentTags();
            enchantmentTagList.addAll(enchants);
            tag.put("Enchantments", enchantmentTagList);
        }

        tile.map(WaterBacktankTile::getCustomName).ifPresent(stack::setHoverName);
        return stack;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
package net.alvin.infinityforge.block;

import net.alvin.infinityforge.block.entity.FakeBlockEntity;
import net.alvin.infinityforge.block.entity.ModBlockEntities;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class FakeBlock extends BlockWithEntity {
    public FakeBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FakeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.FAKE_BLOCK_ENTITY, FakeBlockEntity::tick);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        BlockState d = disguise(world, pos);
        return d != null ? d.getOutlineShape(world, pos, ctx) : super.getOutlineShape(state, world, pos, ctx);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
        BlockState d = disguise(world, pos);
        return d != null ? d.getCollisionShape(world, pos, ctx) : super.getCollisionShape(state, world, pos, ctx);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        BlockState d = disguise(world, pos);
        return d != null ? new ItemStack(d.getBlock()) : ItemStack.EMPTY;
    }

    private BlockState disguise(BlockView world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof FakeBlockEntity be)
            return be.getDisguiseState();
        return null;
    }
}

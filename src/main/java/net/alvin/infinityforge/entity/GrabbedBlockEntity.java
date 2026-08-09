package net.alvin.infinityforge.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class GrabbedBlockEntity extends FallingBlockEntity {
    private boolean held = true;
    public GrabbedBlockEntity(World world, double x, double y, double z, BlockState state) {
        super(world, x, y, z, state);
    }

    public void setHeld(boolean held) {
        this.held = held;
    }

    @Override
    public void move(MovementType type, Vec3d movement) {
        super.move(type, movement);
        if (held) {
            this.setOnGround(false);
            this.timeFalling = 0;
        }
    }

    public void drop() {
        this.setHeld(false);
        this.setNoGravity(false);
    }

    public static GrabbedBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        GrabbedBlockEntity grabbedBlock = new GrabbedBlockEntity(
                world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, state);
        world.setBlockState(pos, state.getFluidState().getBlockState(), Block.NOTIFY_ALL);
        grabbedBlock.dropItem = false;
        grabbedBlock.setNoGravity(true);
        world.spawnEntity(grabbedBlock);
        return grabbedBlock;
    }
}

package net.alvin.infinityforge.helpers;

import net.alvin.infinityforge.InfinityForge;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TeleportationHelper {
    private static final Random RANDOM = new Random();
    private static final int DEFAULT_RADIUS = 2000;
    private static final int END_RADIUS = 200;
    private static final int MAX_ATTEMPTS = 20;

    public static BlockPos getSafeTeleportPos(ServerWorld world) {
        boolean hasCeiling = world.getDimension().hasCeiling();
        int bottomY = world.getBottomY();
        int topY = world.getTopY();
        int radius = world.getRegistryKey().equals(World.END) ? END_RADIUS : DEFAULT_RADIUS;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            InfinityForge.LOGGER.info("TeleportationHelper: Teleportation Attempt {}", attempt);
            int x = RANDOM.nextInt(radius * 2) - radius;
            int z = RANDOM.nextInt(radius * 2) - radius;
            world.getChunk(x >> 4, z >> 4);

            BlockPos candidate = hasCeiling
                    ? scanBottomUp(world, x, z, bottomY + 5, topY - 5)
                    : scanTopDown(world, x, z, bottomY);

            if (candidate != null) {
                InfinityForge.LOGGER.info("TeleportationHelper: Safe Position (X: {}, Y: {}, Z: {}) Found in {} attempts!", candidate.getX(), candidate.getY(), candidate.getZ(), attempt);
                return candidate;
            }
        }

        InfinityForge.LOGGER.info("TeleportationHelper: Teleportation Attempts Failed!");
        return new BlockPos(0, 64, 0);
    }

    // For ceiling dimensions (Nether, custom)
    @Nullable
    private static BlockPos scanBottomUp(ServerWorld world, int x, int z, int minY, int maxY) {
        BlockPos.Mutable pos = new BlockPos.Mutable(x, minY, z);
        for (int y = minY; y < maxY; y++) {
            pos.setY(y);
            BlockState floor = world.getBlockState(pos);
            if (floor.isAir() || floor.getBlock() == Blocks.LAVA) continue;

            BlockState head = world.getBlockState(pos.up(2));
            BlockState body = world.getBlockState(pos.up());
            if (body.isAir() && !body.getBlock().equals(Blocks.LAVA)
                    && head.isAir() && !head.getBlock().equals(Blocks.LAVA)) {
                return pos.up().toImmutable();
            }
        }
        return null;
    }

    // For open dimensions (Overworld, End, custom)
    @Nullable
    private static BlockPos scanTopDown(ServerWorld world, int x, int z, int bottomY) {
        BlockPos top = world.getTopPosition(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                new BlockPos(x, 0, z)
        );

        if (top.getY() <= bottomY) return null;
        //if (world.getBlockState(top.down()).getBlock() == Blocks.LAVA) return null;
        return top;
    }
}


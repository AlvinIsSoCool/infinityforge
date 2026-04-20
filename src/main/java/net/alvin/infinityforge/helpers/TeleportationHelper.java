package net.alvin.infinityforge.helpers;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TeleportationHelper {
    private static final Random RANDOM = new Random();
    private static final int RANDOM_RADIUS = 200;
    private static final int MAX_ATTEMPTS = 4;

    public static BlockPos getSafeTeleportPos(ServerWorld world) {
        boolean hasCeiling = world.getDimension().hasCeiling();
        int bottomY = world.getBottomY();
        int topY = world.getTopY();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int x = RANDOM.nextInt(RANDOM_RADIUS * 2) - RANDOM_RADIUS;
            int z = RANDOM.nextInt(RANDOM_RADIUS * 2) - RANDOM_RADIUS;
            world.getChunk(x >> 4, z >> 4);

            BlockPos candidate = hasCeiling
                    ? scanBottomUp(world, x, z, bottomY + 5, topY - 5)
                    : scanTopDown(world, x, z, bottomY);

            if (candidate != null) return candidate;
        }

        return new BlockPos(0, 64, 0);
    }

    // For ceiling dimensions (Nether, custom)
    @Nullable
    private static BlockPos scanBottomUp(ServerWorld world, int x, int z, int minY, int maxY) {
        for (int y = minY; y < maxY; y++) {
            BlockPos pos = new BlockPos(x, y, z);
            if (!world.getBlockState(pos).isAir()
                    && world.getBlockState(pos.up()).isAir()
                    && world.getBlockState(pos.up().up()).isAir()) {
                return pos.up();
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
        return top;
    }
}


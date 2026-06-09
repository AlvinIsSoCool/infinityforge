package net.alvin.infinityforge.infinity.abilities.impl.space;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class PortalAbility extends ActiveAbility {
    private static final int DEFAULT_SEARCH_RADIUS = 8;
    private static final int NETHER_NEAR_SCAN = 16;
    private static final Set<Block> UNSAFE_BLOCKS = Set.of(
            Blocks.LAVA, Blocks.FIRE, Blocks.SOUL_FIRE,
            Blocks.CACTUS, Blocks.MAGMA_BLOCK, Blocks.WITHER_ROSE,
            Blocks.SWEET_BERRY_BUSH, Blocks.POWDER_SNOW
    );

    public PortalAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        BlockPos pos = getSafeTeleportPos(world, new BlockPos(0, 0, 0), DEFAULT_SEARCH_RADIUS)
                .orElse(world.getSpawnPos());
        InfinityForge.LOGGER.info("Safe Position: X: {}, Y: {}, Z: {}", pos.getX(), pos.getY(), pos.getZ());
        FabricDimensions.teleport(
                player,
                world,
                new TeleportTarget(
                        pos.toCenterPos().add(0.0, 1.0, 0.0),
                        Vec3d.ZERO,
                        player.getYaw(),
                        player.getPitch()
                )
        );
        return true;
    }

    public static Optional<BlockPos> getSafeTeleportPos(ServerWorld world, BlockPos center, int searchRadius) {
        boolean hasCeiling = world.getDimension().hasCeiling();
        int bottomY = world.getBottomY() + 1;
        int topY = world.getTopY() - 2;
        int preferredY = MathHelper.clamp(center.getY(), bottomY, topY);

        world.getChunk(center.getX() >> 4, center.getZ() >> 4);

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int r = 0; r <= searchRadius; r++) {
            if (r == 0) {
                BlockPos c = tryColumn(world, center.getX(), center.getZ(), preferredY, bottomY, topY, hasCeiling, mutable);
                if (c != null) return Optional.of(c);
                continue;
            }

            int x1 = center.getX() - r, x2 = center.getX() + r;
            int z1 = center.getZ() - r, z2 = center.getZ() + r;

            for (int x = x1; x <= x2; x++) {
                BlockPos c = tryColumn(world, x, z1, preferredY, bottomY, topY, hasCeiling, mutable);
                if (c != null) return Optional.of(c);
                c = tryColumn(world, x, z2, preferredY, bottomY, topY, hasCeiling, mutable);
                if (c != null) return Optional.of(c);
            }
            for (int z = z1 + 1; z < z2; z++) {
                BlockPos c = tryColumn(world, x1, z, preferredY, bottomY, topY, hasCeiling, mutable);
                if (c != null) return Optional.of(c);
                c = tryColumn(world, x2, z, preferredY, bottomY, topY, hasCeiling, mutable);
                if (c != null) return Optional.of(c);
            }
        }

        return Optional.empty();
    }

    @Nullable
    private static BlockPos tryColumn(ServerWorld world, int x, int z,
                                      int preferredY, int bottomY, int topY,
                                      boolean hasCeiling, BlockPos.Mutable mutable) {
        if (!world.getChunkManager().isChunkLoaded(x >> 4, z >> 4)) return null;
        return hasCeiling ? scanBottomUp(world, x, z, preferredY, bottomY, topY, mutable)
                : scanTopDown(world, x, z, bottomY, topY, mutable);
    }

    // For Open Dimensions: Heightmap check and player-surrounding block validation.
    @Nullable
    private static BlockPos scanTopDown(ServerWorld world, int x, int z,
                                        int bottomY, int topY, BlockPos.Mutable mutable) {
        BlockPos top = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, new BlockPos(x, 0, z));

        int feetY = top.getY();
        if (feetY < bottomY || feetY > topY) return null;

        mutable.set(x, feetY - 1, z);
        if (notSafeFloor(world.getBlockState(mutable))) return null;

        mutable.setY(feetY);
        if (notSafeSpace(world.getBlockState(mutable))) return null;

        mutable.setY(feetY + 1);
        if (notSafeSpace(world.getBlockState(mutable))) return null;

        return new BlockPos(x, feetY, z);
    }

    // For Ceiling Dimensions: Fast search around preferredY, full search if needed.
    @Nullable
    private static BlockPos scanBottomUp(ServerWorld world, int x, int z,
                                         int preferredY, int minY, int maxY,
                                         BlockPos.Mutable mutable) {
        int nearMin = Math.max(minY, preferredY - NETHER_NEAR_SCAN);
        int nearMax = Math.min(maxY, preferredY + NETHER_NEAR_SCAN);

        BlockPos result = scanRange(world, x, z, nearMin, nearMax, mutable);
        if (result != null) return result;

        if (nearMin > minY) {
            result = scanRange(world, x, z, minY, nearMin - 1, mutable);
            if (result != null) return result;
        }

        if (nearMax < maxY) {
            result = scanRange(world, x, z, nearMax + 1, maxY, mutable);
        }
        return result;
    }

    @Nullable
    private static BlockPos scanRange(ServerWorld world, int x, int z,
                                      int minY, int maxY, BlockPos.Mutable mutable) {
        for (int y = minY; y <= maxY - 2; y++) {
            mutable.set(x, y, z);
            if (notSafeFloor(world.getBlockState(mutable))) continue;

            mutable.setY(y + 1);
            if (notSafeSpace(world.getBlockState(mutable))) continue;

            mutable.setY(y + 2);
            if (notSafeSpace(world.getBlockState(mutable))) continue;

            return new BlockPos(x, y + 1, z);
        }
        return null;
    }

    private static boolean notSafeFloor(BlockState state) {
        if (state.isAir()) return true;
        if (!state.getFluidState().isEmpty()) return true;
        return UNSAFE_BLOCKS.contains(state.getBlock());
    }

    private static boolean notSafeSpace(BlockState state) {
        if (!state.getFluidState().isEmpty()) return true;
        if (UNSAFE_BLOCKS.contains(state.getBlock())) return true;
        return !state.isAir();
    }
}

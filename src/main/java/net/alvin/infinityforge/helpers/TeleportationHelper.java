package net.alvin.infinityforge.helpers;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TeleportationHelper {
    private static final Random RANDOM = new Random();
    private static final int RANDOM_RADIUS = 200;
    private static final int MAX_ATTEMPTS = 4;

    public static TypedActionResult<ItemStack> onSpaceStoneUse(World world, PlayerEntity user, ItemStack stack) {
        if (world.isClient) {
            return TypedActionResult.success(stack);
        }

        MinecraftServer server = world.getServer();
        RegistryKey<World> currentKey = user.getWorld().getRegistryKey();
        Registry<DimensionOptions> registry = server.getRegistryManager().get(RegistryKeys.DIMENSION);
        List<RegistryKey<World>> keys = new ArrayList<>();

        for (RegistryKey<DimensionOptions> dimKey : registry.getKeys()) {
            RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, dimKey.getValue());
            if (!worldKey.getValue().equals(currentKey.getValue())) keys.add(worldKey);
        }

        if (keys.isEmpty()) return TypedActionResult.pass(stack);

        Collections.shuffle(keys);
        for (RegistryKey<World> key : keys) {
            ServerWorld target = server.getWorld(key);
            if (target == null) continue;

            BlockPos safeSpawn = getSafeTeleportPos(target);
            System.out.println("Space Stone: spawnPos: " + safeSpawn.getX() + ", " + safeSpawn.getY() + ", " + safeSpawn.getZ());
            FabricDimensions.teleport(user, target,
                    new TeleportTarget(
                            new Vec3d(safeSpawn.getX() + 0.5, safeSpawn.getY(), safeSpawn.getZ() + 0.5),
                            Vec3d.ZERO,
                            user.getYaw(),
                            user.getPitch()
                    )
            );

            return TypedActionResult.success(stack);
        }

        return TypedActionResult.pass(stack);
    }

    private static BlockPos getSafeTeleportPos(ServerWorld world) {
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


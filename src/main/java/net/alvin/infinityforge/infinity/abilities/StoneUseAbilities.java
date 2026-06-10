package net.alvin.infinityforge.infinity.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Heightmap;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoneUseAbilities {
    private static final Random RANDOM = new Random();
    private static final int DEFAULT_RADIUS = 10000;
    private static final int END_RADIUS = 250;
    private static final int MAX_ATTEMPTS = 10;
    private static final int NETHER_MIN_Y = 40;
    private static final int NETHER_MAX_Y = 90;

    public static TypedActionResult<ItemStack> onSpaceStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stack);

        MinecraftServer server = world.getServer();
        if (server == null) return TypedActionResult.pass(stack);

        RegistryKey<World> currentKey = user.getWorld().getRegistryKey();
        Registry<DimensionOptions> registry = server.getRegistryManager().get(RegistryKeys.DIMENSION);

        List<RegistryKey<World>> keys = new ArrayList<>();
        for (RegistryKey<DimensionOptions> dimKey : registry.getKeys()) {
            RegistryKey<World> worldKey = RegistryKey.of(RegistryKeys.WORLD, dimKey.getValue());
            if (!worldKey.equals(currentKey)) keys.add(worldKey);
        }
        if (keys.isEmpty()) return TypedActionResult.pass(stack);

        ServerWorld targetWorld = server.getWorld(keys.get(RANDOM.nextInt(keys.size())));
        if (targetWorld == null) return TypedActionResult.pass(stack);

        BlockPos teleportPos = getTeleportPos(targetWorld);
        FabricDimensions.teleport(user, targetWorld,
                new TeleportTarget(
                        teleportPos.toCenterPos().add(0.0, 1.0, 0.0),
                        Vec3d.ZERO,
                        user.getYaw(),
                        user.getPitch()
                )
        );

        return TypedActionResult.success(stack);
    }

    public static BlockPos getTeleportPos(ServerWorld world) {
        boolean hasCeiling = world.getDimension().hasCeiling();
        int bottomY = world.getBottomY();
        boolean isEnd = world.getRegistryKey().equals(World.END);
        int radius = isEnd ? END_RADIUS : DEFAULT_RADIUS;

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            InfinityForge.LOGGER.info("TeleportationHelper: Teleportation Attempt {}", attempt+1);
            int x = RANDOM.nextInt(radius * 2) - radius;
            int z = RANDOM.nextInt(radius * 2) - radius;

            BlockPos candidate;
            if (isEnd) candidate = getHeightmapPos(world, x, z, bottomY);
            else if (hasCeiling) candidate = randomYPos(x, z, NETHER_MIN_Y, NETHER_MAX_Y);
            else candidate = randomYPos(x, z, 70, 120);

            if (candidate != null) {
                InfinityForge.LOGGER.info("TeleportationHelper: Position (X: {}, Y: {}, Z: {}) Found in {} attempts!",
                        candidate.getX(), candidate.getY(), candidate.getZ(), attempt+1);
                return candidate;
            }
        }

        return world.getSpawnPos();
    }

    @Nullable
    private static BlockPos randomYPos(int x, int z, int minY, int maxY) {
        if (maxY <= minY) return null;
        int y = minY + RANDOM.nextInt(maxY - minY);
        return new BlockPos(x, y, z);
    }

    @Nullable
    private static BlockPos getHeightmapPos(ServerWorld world, int x, int z, int bottomY) {
        BlockPos top = world.getTopPosition(
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                new BlockPos(x, 0, z)
        );
        return top.getY() <= bottomY ? null : top;
    }

    public static TypedActionResult<ItemStack> onRealityStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stack);

        BlockHitResult hit = (BlockHitResult) user.raycast(5.0, 1.0f, false);
        if (hit.getType() == HitResult.Type.MISS) return TypedActionResult.pass(stack);
        Block randBlock = Registries.BLOCK.get(
                world.getRandom().nextInt(Registries.BLOCK.size()));
        world.setBlockState(hit.getBlockPos(), randBlock.getDefaultState());
        return TypedActionResult.pass(stack);
    }
}

package net.alvin.infinityforge.infinity.abilities.impl;

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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.alvin.infinityforge.helpers.TeleportationHelper.getSafeTeleportPos;

public class StoneUseAbilities {
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

        Collections.shuffle(keys);
        ServerWorld targetWorld = server.getWorld(keys.get(0));
        if (targetWorld == null) return TypedActionResult.pass(stack);

        CompletableFuture.runAsync(() -> {
            BlockPos safeSpawn = getSafeTeleportPos(targetWorld);
            server.execute(() -> FabricDimensions.teleport(user, targetWorld,
                    new TeleportTarget(
                            new Vec3d(safeSpawn.getX() + 0.5, safeSpawn.getY(), safeSpawn.getZ() + 0.5),
                            Vec3d.ZERO,
                            user.getYaw(),
                            user.getPitch()
                    )
            ));
        });

        return TypedActionResult.success(stack);
    }

    public static TypedActionResult<ItemStack> onRealityStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (world.isClient) return TypedActionResult.success(stack);

        BlockHitResult hit = (BlockHitResult) user.raycast(5.0, 1.0f, false);
        Block randBlock = Registries.BLOCK.get(
                world.getRandom().nextInt(Registries.BLOCK.size()));
        world.setBlockState(hit.getBlockPos(), randBlock.getDefaultState());
        return TypedActionResult.pass(stack);
    }
}

package net.alvin.infinityforge.infinity;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.alvin.infinityforge.helpers.TeleportationHelper.getSafeTeleportPos;

public class UseAbilities {
    public static TypedActionResult<ItemStack> onPowerStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.pass(stack);
    }

    public static TypedActionResult<ItemStack> onSpaceStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

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

    public static TypedActionResult<ItemStack> onRealityStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.pass(stack);
    }

    public static TypedActionResult<ItemStack> onSoulStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.pass(stack);
    }

    public static TypedActionResult<ItemStack> onMindStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.pass(stack);
    }

    public static TypedActionResult<ItemStack> onTimeStoneUse(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.pass(stack);
    }
}

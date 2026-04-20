package net.alvin.infinityforge.infinity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class HoldAbilities {
    public static void onPowerStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof PlayerEntity player)) return;

        boolean inMainHand = player.getMainHandStack() == stack;
        boolean inOffHand = player.getOffHandStack() == stack;

        if (inMainHand || inOffHand) {
            ServerWorld serverWorld = (ServerWorld) world;
            Random random = new Random();
            float radius = 5.0f;

            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                    player.getX(), player.getY(), player.getZ(), 20,
                    0.5, 0.5, 0.5, 1.0);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                    player.getX(), player.getY(), player.getZ(), 10,
                    0.5, 0.5, 0.5, 1.0);
            serverWorld.playSound(null, player.getBlockPos(),
                    SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS,
                    0.5f, 1.0f);

            if (world.getTime() % 20 == 0) {
                double angle = random.nextDouble() * Math.PI * 2;
                double r = random.nextDouble() * radius;
                double x = entity.getX() + Math.cos(angle) * r;
                double z = entity.getZ() + Math.sin(angle) * r;
                double y = entity.getY() + (random.nextDouble() * 2 - 1);

                serverWorld.createExplosion(
                        null,
                        x, y, z,
                        5.0f,
                        false,
                        World.ExplosionSourceType.TNT
                );
            }
        }
    }

    public static void onSpaceStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }

    public static void onRealityStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }

    public static void onSoulStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }

    public static void onMindStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }

    public static void onTimeStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }
}
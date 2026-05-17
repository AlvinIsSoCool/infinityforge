package net.alvin.infinityforge.infinity.abilities.impl;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

import java.util.Random;

public class StoneHoldAbilities {
    public static void onPowerStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof PlayerEntity player)) return;

        boolean inOffHand = player.getOffHandStack() == stack;
        if (selected || inOffHand) {
            ServerWorld serverWorld = (ServerWorld) world;
            Random random = new Random();
            float radius = 5.0f;

            serverWorld.spawnParticles(ParticleTypes.PORTAL,
                    player.getX(), player.getY(), player.getZ(), 20,
                    0.5, 0.5, 0.5, 1.5);
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                    player.getX(), player.getY(), player.getZ(), 10,
                    0.5, 0.5, 0.5, 1.5);
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

    public static void onSoulStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;
        if (!(entity instanceof PlayerEntity player)) return;

        boolean inOffHand = player.getOffHandStack() == stack;
        if (selected || inOffHand) {
            if (world.getTime() % 60 == 0) {
                if (player.getHealth() < player.getMaxHealth()) {
                    player.heal(2.0f);
                }
            }

            if (world.random.nextFloat() < 0.05f) {
                player.setHealth(2.0f);
                player.damage(world.getDamageSources().generic(), 1.0f);
            }
        }
    }

    public static void onMindStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }

    public static void onTimeStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

    }
}
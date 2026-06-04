package net.alvin.infinityforge.infinity.abilities.impl;

import net.alvin.infinityforge.effect.HealthDrainStatusEffect;
import net.alvin.infinityforge.registry.ModStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.Random;

public class StoneHoldAbilities {
    public static void onPowerStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;
        if (!(entity instanceof LivingEntity living)) return;

        boolean inOffHand = living.getOffHandStack() == stack;
        if (selected || inOffHand) {
            StatusEffectInstance healthDrain = new StatusEffectInstance(
                    ModStatusEffects.HEALTH_DRAIN_EFFECT, 40, 0, false,
                    false, true);
            StatusEffectInstance scrollLocked = new StatusEffectInstance(
                    ModStatusEffects.SCROLL_LOCKED_EFFECT, 40, 0, false,
                    false, true);
            StatusEffectInstance movementLocked = new StatusEffectInstance(
                    ModStatusEffects.MOVEMENT_LOCKED_EFFECT, 40, 0, false,
                    false, true);
            StatusEffectInstance blindness = new StatusEffectInstance(
                    StatusEffects.BLINDNESS, 40, 0, false,
                    false, true);

            living.addStatusEffect(healthDrain);
            living.addStatusEffect(scrollLocked);
            living.addStatusEffect(movementLocked);
            living.addStatusEffect(blindness);

            ServerWorld serverWorld = (ServerWorld) world;
            Random random = new Random();
            float radius = 5.0f;

            serverWorld.spawnParticles(ParticleTypes.EXPLOSION,
                    living.getX(), living.getY(), living.getZ(), 5,
                    0.5, 0.5, 0.5, 1.5);
            serverWorld.playSound(null, living.getBlockPos(),
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
                        world.getRandom().nextBetween(2, 5),
                        false,
                        World.ExplosionSourceType.TNT
                );
            }
        } else {
            // TODO: Check whether this can be avoided, if more than one power stone is present.
            EntityAttributeInstance attr = living.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
            if (attr == null) return;
            attr.removeModifier(HealthDrainStatusEffect.HEALTH_DRAIN_UUID);
        }
    }

    public static void onSoulStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;
        if (!(entity instanceof LivingEntity living)) return;

        boolean inOffHand = living.getOffHandStack() == stack;
        if (selected || inOffHand) {
            if (world.getTime() % 60 == 0) {
                if (living.getHealth() < living.getMaxHealth()) {
                    living.heal(2.0f);
                }
            }

            if (world.random.nextFloat() < 0.01f) {
                living.setHealth(2.0f);
                living.damage(world.getDamageSources().generic(), 1.0f);
            }
        }
    }

    public static void onTimeStoneHold(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;
        if (!(entity instanceof LivingEntity living)) return;

        boolean inOffHand = living.getOffHandStack() == stack;
        if (selected || inOffHand) {
            Box box = living.getBoundingBox().expand(16);
            StatusEffectInstance slowness = new StatusEffectInstance(StatusEffects.SLOWNESS, 40, 3);
            world.getEntitiesByClass(LivingEntity.class, box, le -> le.isAlive() && le != living)
                    .forEach(le -> le.addStatusEffect(slowness));
        }
    }
}
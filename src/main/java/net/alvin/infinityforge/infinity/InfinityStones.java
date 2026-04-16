package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.ModAbilities;
import net.alvin.infinityforge.helpers.TeleportationHelper;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class InfinityStones {
    public static final InfinityStoneType POWER = register(
            "power",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {
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
                    },
                    List.of(ModAbilities.TEST),
                    0x8700D3,
                    0x9605FF
            )
    );

    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    (world, user, hand) -> TeleportationHelper.onSpaceStoneUse(world, user, user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0x0255FF,
                    0x6AC5FF
            )
    );

    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0x6E0000,
                    0xFF0000
            )
    );

    public static final InfinityStoneType SOUL = register(
            "soul",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0xFF3C05,
                    0xFF5B0F
            )
    );

    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0xFFDE00,
                    0xFFFF00
            )
    );

    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0x05A005,
                    0x00FF00
            )
    );

    private static InfinityStoneType register(String name, InfinityStoneType type) {
        return Registry.register(
                InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY,
                new Identifier(InfinityForge.MOD_ID, name),
                type
        );
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Infinity Stone Types for " + InfinityForge.MOD_ID);
    }
}

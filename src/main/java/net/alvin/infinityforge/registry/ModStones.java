package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.helpers.TeleportationHelper;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.InfinityStoneTypeRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static net.alvin.infinityforge.helpers.InfinityStoneColors.*;

public class ModStones {
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
                    List.of(),
                    POWER_STONE_BASE_COLOR,
                    POWER_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    (world, user, hand) -> TeleportationHelper.onSpaceStoneUse(world, user, user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    SPACE_STONE_BASE_COLOR,
                    SPACE_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(ModAbilities.WEATHER, ModAbilities.WEATHER_TOGGLE, ModAbilities.WEATHER_HELD),
                    REALITY_STONE_BASE_COLOR,
                    REALITY_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType SOUL = register(
            "soul",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(ModAbilities.HEALING),
                    SOUL_STONE_BASE_COLOR,
                    SOUL_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(ModAbilities.AB_1, ModAbilities.AB_2, ModAbilities.AB_3, ModAbilities.AB_4),
                    MIND_STONE_BASE_COLOR,
                    MIND_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    TIME_STONE_BASE_COLOR,
                    TIME_STONE_GLINT_COLOR
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

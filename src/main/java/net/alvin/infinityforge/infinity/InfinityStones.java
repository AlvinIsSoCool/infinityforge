package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;

import java.util.List;

public class InfinityStones {
    public static final InfinityStoneType POWER = register(
            "power_stone",
            new InfinityStoneType(
                    (world, user, hand) -> {
                        user.playSound(Blocks.AMETHYST_BLOCK.getSoundGroup(Blocks.AMETHYST_BLOCK.getDefaultState()).getBreakSound(), 1.0F, 1.0F);
                        return TypedActionResult.success(user.getStackInHand(hand));
                    },
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0x8700D3,
                    0x9605FF
            )
    );

    public static final InfinityStoneType SPACE = register(
            "space_stone",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0x0255FF,
                    0x6AC5FF
            )
    );

    public static final InfinityStoneType REALITY = register(
            "reality_stone",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0x6E0000,
                    0xFF0000
            )
    );

    public static final InfinityStoneType SOUL = register(
            "soul_stone",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0xFF3C05,
                    0xFF5B0F
            )
    );

    public static final InfinityStoneType MIND = register(
            "mind_stone",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of(),
                    0xFFDE00,
                    0xFFFF00
            )
    );

    public static final InfinityStoneType TIME = register(
            "time_stone",
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

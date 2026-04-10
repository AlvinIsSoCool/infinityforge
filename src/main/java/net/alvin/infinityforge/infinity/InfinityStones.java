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
            "power",
            new InfinityStoneType(
                    (world, user, hand) -> {
                        user.playSound(Blocks.AMETHYST_BLOCK.getSoundGroup(Blocks.AMETHYST_BLOCK.getDefaultState()).getBreakSound(), 1.0F, 1.0F);
                        return TypedActionResult.success(user.getStackInHand(hand));
                    },
                    (stack, world, entity, slot, selected) -> {},
                    List.of()
            )
    );

    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of()
            )
    );

    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of()
            )
    );

    public static final InfinityStoneType SOUL = register(
            "soul",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of()
            )
    );

    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of()
            )
    );

    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    (world, user, hand) -> TypedActionResult.pass(user.getStackInHand(hand)),
                    (stack, world, entity, slot, selected) -> {},
                    List.of()
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

package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.ModAbilities;
import net.alvin.infinityforge.infinity.HoldAbilities;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.infinity.UseAbilities;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import java.util.List;

import static net.alvin.infinityforge.helpers.InfinityStoneColors.*;

public class ModStones {
    public static final InfinityStoneType POWER = register(
            "power",
            new InfinityStoneType(
                    UseAbilities::onPowerStoneUse,
                    HoldAbilities::onPowerStoneHold,
                    List.of(),
                    POWER_STONE_BASE_COLOR,
                    POWER_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    UseAbilities::onSpaceStoneUse,
                    HoldAbilities::onSpaceStoneHold,
                    List.of(ModAbilities.TELEPORT),
                    SPACE_STONE_BASE_COLOR,
                    SPACE_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    UseAbilities::onRealityStoneUse,
                    HoldAbilities::onRealityStoneHold,
                    List.of(
                            ModAbilities.WEATHER, ModAbilities.REAL_CHANGE_BLOCK
                    ),
                    REALITY_STONE_BASE_COLOR,
                    REALITY_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType SOUL = register(
            "soul",
            new InfinityStoneType(
                    UseAbilities::onSoulStoneUse,
                    HoldAbilities::onSoulStoneHold,
                    List.of(
                            ModAbilities.HEALING, ModAbilities.HEALTH,
                            ModAbilities.SATURATION, ModAbilities.WATER_BREATHING,
                            ModAbilities.KILL
                    ),
                    SOUL_STONE_BASE_COLOR,
                    SOUL_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    UseAbilities::onMindStoneUse,
                    HoldAbilities::onMindStoneHold,
                    List.of(),
                    MIND_STONE_BASE_COLOR,
                    MIND_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    UseAbilities::onTimeStoneUse,
                    HoldAbilities::onTimeStoneHold,
                    List.of(),
                    TIME_STONE_BASE_COLOR,
                    TIME_STONE_GLINT_COLOR
            )
    );

    public static final List<InfinityStoneType> ALL_STONES = List.of(
            ModStones.POWER, ModStones.SPACE,
            ModStones.REALITY, ModStones.SOUL,
            ModStones.MIND, ModStones.TIME
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

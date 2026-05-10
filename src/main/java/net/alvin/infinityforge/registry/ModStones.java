package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.ModAbilities;
import net.alvin.infinityforge.helpers.InfinityStoneColors;
import net.alvin.infinityforge.infinity.HoldAbilities;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.infinity.UseAbilities;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import java.util.List;

public class ModStones {
    public static final InfinityStoneType POWER = register(
            "power",
            new InfinityStoneType(
                    UseAbilities::onPowerStoneUse,
                    HoldAbilities::onPowerStoneHold,
                    List.of(),
                    InfinityStoneColors.POWER_STONE_BASE_COLOR,
                    InfinityStoneColors.POWER_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    UseAbilities::onSpaceStoneUse,
                    null,
                    List.of(ModAbilities.TELEPORT, ModAbilities.FORCEFIELD,
                            ModAbilities.PHASING),
                    InfinityStoneColors.SPACE_STONE_BASE_COLOR,
                    InfinityStoneColors.SPACE_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    UseAbilities::onRealityStoneUse,
                    null,
                    List.of(
                            ModAbilities.WEATHER, ModAbilities.SPAWN_REAL_BLOCK,
                            ModAbilities.SPAWN_FAKE_ITEM
                    ),
                    InfinityStoneColors.REALITY_STONE_BASE_COLOR,
                    InfinityStoneColors.REALITY_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType SOUL = register(
            "soul",
            new InfinityStoneType(
                    null,
                    HoldAbilities::onSoulStoneHold,
                    List.of(
                            ModAbilities.HEALING, ModAbilities.HEALTH,
                            ModAbilities.SATURATION, ModAbilities.WATER_BREATHING,
                            ModAbilities.KILL, ModAbilities.SNAP
                    ),
                    InfinityStoneColors.SOUL_STONE_BASE_COLOR,
                    InfinityStoneColors.SOUL_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    UseAbilities::onMindStoneUse,
                    HoldAbilities::onMindStoneHold,
                    List.of(ModAbilities.FLIGHT),
                    InfinityStoneColors.MIND_STONE_BASE_COLOR,
                    InfinityStoneColors.MIND_STONE_GLINT_COLOR
            )
    );

    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    UseAbilities::onTimeStoneUse,
                    HoldAbilities::onTimeStoneHold,
                    List.of(ModAbilities.ADVANCE_TIME, ModAbilities.REWIND_TIME,
                            ModAbilities.STOP_TIME, ModAbilities.RANDOMISE_TIME),
                    InfinityStoneColors.TIME_STONE_BASE_COLOR,
                    InfinityStoneColors.TIME_STONE_GLINT_COLOR
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

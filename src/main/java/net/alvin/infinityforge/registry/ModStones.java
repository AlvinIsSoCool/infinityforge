package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.helpers.InfinityStoneColors;
import net.alvin.infinityforge.infinity.abilities.impl.StoneHoldAbilities;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.impl.StoneUseAbilities;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import java.util.List;

public class ModStones {
    public static final InfinityStoneType POWER = register(
            "power",
            new InfinityStoneType(
                    StoneUseAbilities::onPowerStoneUse,
                    StoneHoldAbilities::onPowerStoneHold,
                    List.of(),
                    InfinityStoneColors.POWER_STONE_BASE_COLOR,
                    InfinityStoneColors.POWER_STONE_GLINT_COLOR
            )
    );
    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    StoneUseAbilities::onSpaceStoneUse,
                    null,
                    List.of(ModGauntletAbilities.TELEPORT, ModGauntletAbilities.FORCEFIELD,
                            ModGauntletAbilities.PHASING),
                    InfinityStoneColors.SPACE_STONE_BASE_COLOR,
                    InfinityStoneColors.SPACE_STONE_GLINT_COLOR
            )
    );
    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    StoneUseAbilities::onRealityStoneUse,
                    null,
                    List.of(
                            ModGauntletAbilities.WEATHER, ModGauntletAbilities.SPAWN_REAL_BLOCK,
                            ModGauntletAbilities.SPAWN_FAKE_ITEM
                    ),
                    InfinityStoneColors.REALITY_STONE_BASE_COLOR,
                    InfinityStoneColors.REALITY_STONE_GLINT_COLOR
            )
    );
    public static final InfinityStoneType SOUL = register(
            "soul",
            new InfinityStoneType(
                    null,
                    StoneHoldAbilities::onSoulStoneHold,
                    List.of(
                            ModGauntletAbilities.HEALING, ModGauntletAbilities.HEALTH,
                            ModGauntletAbilities.SATURATION, ModGauntletAbilities.WATER_BREATHING,
                            ModGauntletAbilities.KILL, ModGauntletAbilities.SNAP
                    ),
                    InfinityStoneColors.SOUL_STONE_BASE_COLOR,
                    InfinityStoneColors.SOUL_STONE_GLINT_COLOR
            )
    );
    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    StoneUseAbilities::onMindStoneUse,
                    StoneHoldAbilities::onMindStoneHold,
                    List.of(ModGauntletAbilities.FLIGHT),
                    InfinityStoneColors.MIND_STONE_BASE_COLOR,
                    InfinityStoneColors.MIND_STONE_GLINT_COLOR
            )
    );
    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    StoneUseAbilities::onTimeStoneUse,
                    StoneHoldAbilities::onTimeStoneHold,
                    List.of(ModGauntletAbilities.ADVANCE_TIME, ModGauntletAbilities.REWIND_TIME,
                            ModGauntletAbilities.STOP_TIME, ModGauntletAbilities.RANDOMISE_TIME),
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
                InfinityStoneTypeRegistry.REGISTRY,
                new Identifier(InfinityForge.MOD_ID, name),
                type
        );
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Infinity Stone Types for " + InfinityForge.MOD_ID);
    }
}

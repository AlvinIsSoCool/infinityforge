package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.abilities.StoneHoldAbilities;
import net.alvin.infinityforge.infinity.abilities.StoneUseAbilities;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.infinity.abilities.ModGauntletAbilities;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import java.util.List;

public class ModStones {
    public static final InfinityStoneType POWER = register(
            "power",
            new InfinityStoneType(
                    null,
                    StoneHoldAbilities::onPowerStoneHold,
                    List.of(
                            ModGauntletAbilities.ENERGY_BLAST, ModGauntletAbilities.ENERGY_BEAM,
                            ModGauntletAbilities.KNOCKBACK_RESISTANCE, ModGauntletAbilities.SPEED,
                            ModGauntletAbilities.ATTACK_SPEED, ModGauntletAbilities.STEP_HEIGHT
                    ),
                    () -> InfinityForgeConfig.get().colorOptions.stoneBaseColors.powerStone,
                    () -> InfinityForgeConfig.get().colorOptions.stoneGlintColors.powerStone
            )
    );
    public static final InfinityStoneType SPACE = register(
            "space",
            new InfinityStoneType(
                    StoneUseAbilities::onSpaceStoneUse,
                    null,
                    List.of(
                            ModGauntletAbilities.TELEPORT, ModGauntletAbilities.PORTAL,
                            ModGauntletAbilities.BLACKHOLE, ModGauntletAbilities.FORCEFIELD,
                            ModGauntletAbilities.PHASING
                    ),
                    () -> InfinityForgeConfig.get().colorOptions.stoneBaseColors.spaceStone,
                    () -> InfinityForgeConfig.get().colorOptions.stoneGlintColors.spaceStone
            )
    );
    public static final InfinityStoneType REALITY = register(
            "reality",
            new InfinityStoneType(
                    StoneUseAbilities::onRealityStoneUse,
                    null,
                    List.of(
                            ModGauntletAbilities.WEATHER, ModGauntletAbilities.INVISIBILITY,
                            ModGauntletAbilities.SIZE_CHANGE_BIG, ModGauntletAbilities.SIZE_CHANGE_SMALL,
                            ModGauntletAbilities.TURN_INTO_BUBBLES, ModGauntletAbilities.SPAWN_REAL_BLOCK,
                            ModGauntletAbilities.SPAWN_FAKE_BLOCK, ModGauntletAbilities.SPAWN_REAL_ITEM,
                            ModGauntletAbilities.SPAWN_FAKE_ITEM
                    ),
                    () -> InfinityForgeConfig.get().colorOptions.stoneBaseColors.realityStone,
                    () -> InfinityForgeConfig.get().colorOptions.stoneGlintColors.realityStone
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
                    () -> InfinityForgeConfig.get().colorOptions.stoneBaseColors.soulStone,
                    () -> InfinityForgeConfig.get().colorOptions.stoneGlintColors.soulStone
            )
    );
    public static final InfinityStoneType MIND = register(
            "mind",
            new InfinityStoneType(
                    null,
                    null,
                    List.of(ModGauntletAbilities.FLIGHT, ModGauntletAbilities.CHANGE_SNAP),
                    () -> InfinityForgeConfig.get().colorOptions.stoneBaseColors.mindStone,
                    () -> InfinityForgeConfig.get().colorOptions.stoneGlintColors.mindStone
            )
    );
    public static final InfinityStoneType TIME = register(
            "time",
            new InfinityStoneType(
                    null,
                    StoneHoldAbilities::onTimeStoneHold,
                    List.of(
                            ModGauntletAbilities.ADVANCE_TIME, ModGauntletAbilities.REWIND_TIME,
                            ModGauntletAbilities.STOP_TIME, ModGauntletAbilities.RANDOMISE_TIME
                    ),
                    () -> InfinityForgeConfig.get().colorOptions.stoneBaseColors.timeStone,
                    () -> InfinityForgeConfig.get().colorOptions.stoneGlintColors.timeStone
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

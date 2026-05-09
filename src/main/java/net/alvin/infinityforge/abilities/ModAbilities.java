package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.base.GauntletAbility;
import net.alvin.infinityforge.abilities.impl.mind.FlightAbility;
import net.alvin.infinityforge.abilities.impl.reality.WeatherAbility;
import net.alvin.infinityforge.abilities.impl.reality.SpawnRealBlockAbility;
import net.alvin.infinityforge.abilities.impl.soul.*;
import net.alvin.infinityforge.abilities.impl.space.ForcefieldAbility;
import net.alvin.infinityforge.abilities.impl.space.TeleportAbility;
import net.alvin.infinityforge.abilities.impl.time.AdvanceTimeAbility;
import net.alvin.infinityforge.abilities.impl.time.RandomiseTimeAbility;
import net.alvin.infinityforge.abilities.impl.time.RewindTimeAbility;
import net.alvin.infinityforge.abilities.impl.time.StopTimeAbility;
import net.alvin.infinityforge.abilities.registry.GauntletAbilityRegistry;
import net.alvin.infinityforge.registry.ModStones;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.alvin.infinityforge.helpers.InfinityStoneColors.*;

public class ModAbilities {

    public static final GauntletAbility TELEPORT = GauntletAbilityRegistry.register(
            new TeleportAbility(
                    new Identifier(InfinityForge.MOD_ID, "teleport"),
                    new Identifier("minecraft", "textures/item/ender_pearl.png"),
                    "abilities." + InfinityForge.MOD_ID + ".teleport",
                    SPACE_STONE_ABILITY_COLOR,
                    List::of, 100
            )
    );

    public static final GauntletAbility FORCEFIELD = GauntletAbilityRegistry.register(
            new ForcefieldAbility(
                    new Identifier(InfinityForge.MOD_ID, "forcefield"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/forcefield.png"),
                    "abilities." + InfinityForge.MOD_ID + ".forcefield",
                    SPACE_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );

    public static final GauntletAbility WEATHER = GauntletAbilityRegistry.register(
            new WeatherAbility(
                    new Identifier(InfinityForge.MOD_ID, "weather"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/weather.png"),
                    "abilities." + InfinityForge.MOD_ID + ".weather",
                    REALITY_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100
            )
    );

    public static final GauntletAbility SPAWN_REAL_BLOCK = GauntletAbilityRegistry.register(
            new SpawnRealBlockAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_real_block"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/spawn_real_block.png"),
                    "abilities." + InfinityForge.MOD_ID + ".spawn_real_block",
                    REALITY_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100
            )
    );

    public static final GauntletAbility KILL = GauntletAbilityRegistry.register(
            new KillAbility(
                    new Identifier(InfinityForge.MOD_ID, "kill"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/kill.png"),
                    "abilities." + InfinityForge.MOD_ID + ".kill",
                    SOUL_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.SOUL), 100
            )
    );

    public static final GauntletAbility HEALTH = GauntletAbilityRegistry.register(
            new HealthAbility(
                    new Identifier(InfinityForge.MOD_ID, "health"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/health.png"),
                    "abilities." + InfinityForge.MOD_ID + ".health",
                    SOUL_STONE_ABILITY_COLOR,
                    Map.of(
                            EntityAttributes.GENERIC_MAX_HEALTH,
                            new EntityAttributeModifier(
                                    UUID.randomUUID(),
                                    "Health",
                                    20,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            )
    );

    public static final GauntletAbility HEALING = GauntletAbilityRegistry.register(
            new HealingAbility(
                    new Identifier(InfinityForge.MOD_ID, "healing"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/healing.png"),
                    "abilities." + InfinityForge.MOD_ID + ".healing",
                    SOUL_STONE_ABILITY_COLOR
            )
    );

    public static final GauntletAbility SATURATION = GauntletAbilityRegistry.register(
            new SaturationAbility(
                    new Identifier(InfinityForge.MOD_ID, "saturation"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/saturation.png"),
                    "abilities." + InfinityForge.MOD_ID + ".saturation",
                    SOUL_STONE_ABILITY_COLOR
            )
    );

    public static final GauntletAbility WATER_BREATHING = GauntletAbilityRegistry.register(
            new WaterBreathingAbility(
                    new Identifier(InfinityForge.MOD_ID, "water_breathing"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/water_breathing.png"),
                    "abilities." + InfinityForge.MOD_ID + ".water_breathing",
                    SOUL_STONE_ABILITY_COLOR
            )
    );

    public static final GauntletAbility FLIGHT = GauntletAbilityRegistry.register(
            new FlightAbility(
                    new Identifier(InfinityForge.MOD_ID, "flight"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/flight.png"),
                    "abilities." + InfinityForge.MOD_ID + ".flight",
                    MIND_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.MIND),
                    -1, 0
            )
    );

    public static final GauntletAbility SNAP = GauntletAbilityRegistry.register(
            new SnapAbility(
                    new Identifier(InfinityForge.MOD_ID, "snap"),
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/snap.png"),
                    "abilities." + InfinityForge.MOD_ID + ".snap",
                    RAINBOW_ABILITY_COLOR,
                    () -> ModStones.ALL_STONES,
                    20
            )
    );

    public static final GauntletAbility ADVANCE_TIME = GauntletAbilityRegistry.register(
            new AdvanceTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "advance_time"),
                    new Identifier("minecraft", "textures/item/clock_16.png"),
                    "abilities." + InfinityForge.MOD_ID + ".advance_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );

    public static final GauntletAbility REWIND_TIME = GauntletAbilityRegistry.register(
            new RewindTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "rewind_time"),
                    new Identifier("minecraft", "textures/item/clock_16.png"),
                    "abilities." + InfinityForge.MOD_ID + ".rewind_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );

    public static final GauntletAbility STOP_TIME = GauntletAbilityRegistry.register(
            new StopTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "stop_time"),
                    new Identifier("minecraft", "textures/item/clock_16.png"),
                    "abilities." + InfinityForge.MOD_ID + ".stop_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );

    public static final GauntletAbility RANDOMISE_TIME = GauntletAbilityRegistry.register(
            new RandomiseTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "randomise_time"),
                    new Identifier("minecraft", "textures/item/clock_16.png"),
                    "abilities." + InfinityForge.MOD_ID + ".randomise_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of, 100
            )
    );
}

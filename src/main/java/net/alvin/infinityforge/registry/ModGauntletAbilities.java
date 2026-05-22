package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.ext.AttributeModifierAbility;
import net.alvin.infinityforge.infinity.abilities.impl.mind.*;
import net.alvin.infinityforge.infinity.abilities.impl.reality.*;
import net.alvin.infinityforge.infinity.abilities.impl.soul.*;
import net.alvin.infinityforge.infinity.abilities.impl.space.*;
import net.alvin.infinityforge.infinity.abilities.impl.time.*;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static net.alvin.infinityforge.helpers.InfinityStoneColors.*;

public class ModGauntletAbilities {
    static {
        AbilityIcon.setSheet(new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/ability_icons.png"));
    }

    private static final AbilityIcon.Allocator PASSIVE_ICONS =
            new AbilityIcon.Allocator(
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/passive_ability_icons.png"),
                    0);

    public static final GauntletAbility KNOCKBACK_RESISTANCE = GauntletAbilityRegistry.register(
            new AttributeModifierAbility(
                    new Identifier(InfinityForge.MOD_ID, "knockback_resistance"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.knockback_resistance",
                    POWER_STONE_ABILITY_COLOR,
                    Map.of(
                            EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                            new EntityAttributeModifier(
                                    UUID.randomUUID(),
                                    "Knockback Resistance",
                                    1.0,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            ) {}
    );

    public static final GauntletAbility SPEED = GauntletAbilityRegistry.register(
            new AttributeModifierAbility(
                    new Identifier(InfinityForge.MOD_ID, "speed"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.speed",
                    POWER_STONE_ABILITY_COLOR,
                    Map.of(
                            EntityAttributes.GENERIC_MOVEMENT_SPEED,
                            new EntityAttributeModifier(
                                    UUID.randomUUID(),
                                    "Speed",
                                    0.15,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            ) {}
    );

    public static final GauntletAbility ATTACK_SPEED = GauntletAbilityRegistry.register(
            new AttributeModifierAbility(
                    new Identifier(InfinityForge.MOD_ID, "attack_speed"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.attack_speed",
                    POWER_STONE_ABILITY_COLOR,
                    Map.of(
                            EntityAttributes.GENERIC_ATTACK_SPEED,
                            new EntityAttributeModifier(
                                    UUID.randomUUID(),
                                    "Attack Speed",
                                    2.0,
                                    EntityAttributeModifier.Operation.ADDITION
                            )
                    )
            ) {}
    );

    public static final GauntletAbility TELEPORT = GauntletAbilityRegistry.register(
            new TeleportAbility(
                    new Identifier(InfinityForge.MOD_ID, "teleport"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.teleport",
                    SPACE_STONE_ABILITY_COLOR,
                    List::of, 100
            )
    );
    public static final GauntletAbility FORCEFIELD = GauntletAbilityRegistry.register(
            new ForcefieldAbility(
                    new Identifier(InfinityForge.MOD_ID, "forcefield"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.forcefield",
                    SPACE_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility PHASING = GauntletAbilityRegistry.register(
            new PhasingAbility(
                    new Identifier(InfinityForge.MOD_ID, "phasing"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.phasing",
                    SPACE_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.SPACE, ModStones.MIND),
                    -1, 0
            )
    );
    public static final GauntletAbility WEATHER = GauntletAbilityRegistry.register(
            new WeatherAbility(
                    new Identifier(InfinityForge.MOD_ID, "weather"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.weather",
                    REALITY_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100
            )
    );
    public static final GauntletAbility SPAWN_REAL_BLOCK = GauntletAbilityRegistry.register(
            new SpawnBlockAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_real_block"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.spawn_real_block",
                    REALITY_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100,
                    false
            )
    );
    public static final GauntletAbility SPAWN_FAKE_ITEM = GauntletAbilityRegistry.register(
            new SpawnItemAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_fake_item"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.spawn_fake_item",
                    REALITY_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.REALITY),
                    100, true
            )
    );
    public static final GauntletAbility KILL = GauntletAbilityRegistry.register(
            new KillAbility(
                    new Identifier(InfinityForge.MOD_ID, "kill"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.kill",
                    SOUL_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.SOUL), 100
            )
    );
    public static final GauntletAbility HEALTH = GauntletAbilityRegistry.register(
            new HealthAbility(
                    new Identifier(InfinityForge.MOD_ID, "health"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.health",
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
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.healing",
                    SOUL_STONE_ABILITY_COLOR
            )
    );
    public static final GauntletAbility SATURATION = GauntletAbilityRegistry.register(
            new SaturationAbility(
                    new Identifier(InfinityForge.MOD_ID, "saturation"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.saturation",
                    SOUL_STONE_ABILITY_COLOR
            )
    );
    public static final GauntletAbility WATER_BREATHING = GauntletAbilityRegistry.register(
            new WaterBreathingAbility(
                    new Identifier(InfinityForge.MOD_ID, "water_breathing"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.water_breathing",
                    SOUL_STONE_ABILITY_COLOR
            )
    );
    public static final GauntletAbility FLIGHT = GauntletAbilityRegistry.register(
            new FlightAbility(
                    new Identifier(InfinityForge.MOD_ID, "flight"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.flight",
                    MIND_STONE_ABILITY_COLOR,
                    () -> List.of(ModStones.POWER, ModStones.MIND),
                    -1, 0
            )
    );
    public static final GauntletAbility SNAP = GauntletAbilityRegistry.register(
            new SnapAbility(
                    new Identifier(InfinityForge.MOD_ID, "snap"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.snap",
                    RAINBOW_ABILITY_COLOR,
                    () -> ModStones.ALL_STONES,
                    20
            )
    );
    public static final GauntletAbility ADVANCE_TIME = GauntletAbilityRegistry.register(
            new AdvanceTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "advance_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.advance_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility REWIND_TIME = GauntletAbilityRegistry.register(
            new RewindTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "rewind_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.rewind_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility STOP_TIME = GauntletAbilityRegistry.register(
            new StopTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "stop_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.stop_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility RANDOMISE_TIME = GauntletAbilityRegistry.register(
            new RandomiseTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "randomise_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.randomise_time",
                    TIME_STONE_ABILITY_COLOR,
                    List::of, 100
            )
    );
}

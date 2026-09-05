package net.alvin.infinityforge.infinity.abilities.impl;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.config.client.InfinityForgeClientConfig;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.base.AttributeModifierAbility;
import net.alvin.infinityforge.infinity.abilities.base.LifecyclePassiveAbility;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.abilities.icon.ModAbilityIcons;
import net.alvin.infinityforge.infinity.abilities.impl.mind.*;
import net.alvin.infinityforge.infinity.abilities.impl.power.EnergyBeamAbility;
import net.alvin.infinityforge.infinity.abilities.impl.power.EnergyBlastAbility;
import net.alvin.infinityforge.infinity.abilities.impl.power.ExplosionAbility;
import net.alvin.infinityforge.infinity.abilities.impl.reality.*;
import net.alvin.infinityforge.infinity.abilities.impl.soul.*;
import net.alvin.infinityforge.infinity.abilities.impl.space.*;
import net.alvin.infinityforge.infinity.abilities.impl.time.*;
import net.alvin.infinityforge.network.s2c.SyncStepHeightS2CPacket;
import net.alvin.infinityforge.registry.GauntletAbilityRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ModGauntletAbilities {
    public static final GauntletAbility ENERGY_BLAST = GauntletAbilityRegistry.register(
            new EnergyBlastAbility(
                    new Identifier(InfinityForge.MOD_ID, "energy_blast"),
                    ModAbilityIcons.ENERGY_BLAST,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of, 20
            )
    );
    public static final GauntletAbility ENERGY_BEAM = GauntletAbilityRegistry.register(
            new EnergyBeamAbility(
                    new Identifier(InfinityForge.MOD_ID, "energy_beam"),
                    ModAbilityIcons.ENERGY_BEAM,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of,
                    400, -2
            )
    );
    public static final GauntletAbility EXPLOSION = GauntletAbilityRegistry.register(
            new ExplosionAbility(
                    new Identifier(InfinityForge.MOD_ID, "explosion"),
                    ModAbilityIcons.EXPLOSION,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of, 40, 4
            )
    );
    public static final GauntletAbility ATTACK_STRENGTH = GauntletAbilityRegistry.register(
            new PassiveAbility(
                    new Identifier(InfinityForge.MOD_ID, "attack_strength"),
                    ModAbilityIcons.ATTACK_STRENGTH,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of
            ) {}
    );
    public static final GauntletAbility KNOCKBACK_RESISTANCE = GauntletAbilityRegistry.register(
            new AttributeModifierAbility(
                    new Identifier(InfinityForge.MOD_ID, "knockback_resistance"),
                    ModAbilityIcons.KNOCKBACK_RESISTANCE,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of,
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
                    ModAbilityIcons.SPEED,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of,
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
                    ModAbilityIcons.ATTACK_SPEED,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of,
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
    public static final GauntletAbility STEP_HEIGHT = GauntletAbilityRegistry.register(
            new LifecyclePassiveAbility(
                    new Identifier(InfinityForge.MOD_ID, "step_height"),
                    ModAbilityIcons.STEP_HEIGHT,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.powerStone,
                    List::of
                    ) {
                @Override
                public void onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
                    player.setStepHeight(1.0f);
                    ServerPlayNetworking.send(player, new SyncStepHeightS2CPacket(1.0f));
                }

                @Override
                public void onEnd(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
                    player.setStepHeight(0.6f);
                    ServerPlayNetworking.send(player, new SyncStepHeightS2CPacket(0.6f));
                }
            }
    );
    public static final GauntletAbility TELEPORT = GauntletAbilityRegistry.register(
            new TeleportAbility(
                    new Identifier(InfinityForge.MOD_ID, "teleport"),
                    ModAbilityIcons.TELEPORT,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.spaceStone,
                    List::of, 100
            )
    );
    public static final GauntletAbility PORTAL = GauntletAbilityRegistry.register(
            new PortalAbility(
                    new Identifier(InfinityForge.MOD_ID, "portal"),
                    ModAbilityIcons.PORTAL,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.spaceStone,
                    List::of, 0
            )
    );
    public static final GauntletAbility BLACKHOLE = GauntletAbilityRegistry.register(
            new BlackHoleAbility(
                    new Identifier(InfinityForge.MOD_ID, "blackhole"),
                    ModAbilityIcons.BLACKHOLE,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.spaceStone,
                    List::of,
                    400, -1
            )
    );
    public static final GauntletAbility FORCEFIELD = GauntletAbilityRegistry.register(
            new ForcefieldAbility(
                    new Identifier(InfinityForge.MOD_ID, "forcefield"),
                    ModAbilityIcons.FORCEFIELD,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.spaceStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility PHASING = GauntletAbilityRegistry.register(
            new PhasingAbility(
                    new Identifier(InfinityForge.MOD_ID, "phasing"),
                    ModAbilityIcons.PHASING,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.spaceStone,
                    () -> List.of(ModStones.SPACE, ModStones.MIND),
                    -1, 0
            )
    );
    public static final GauntletAbility WEATHER = GauntletAbilityRegistry.register(
            new WeatherAbility(
                    new Identifier(InfinityForge.MOD_ID, "weather"),
                    ModAbilityIcons.WEATHER,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100
            )
    );
    public static final GauntletAbility INVISIBILITY = GauntletAbilityRegistry.register(
            new InvisibilityAbility(
                    new Identifier(InfinityForge.MOD_ID, "invisibility"),
                    ModAbilityIcons.INVISIBILITY,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    List::of,
                    -1, 0
            )
    );
    public static final GauntletAbility SIZE_CHANGE_SMALL = GauntletAbilityRegistry.register(
            new SizeChangeAbility(
                    new Identifier(InfinityForge.MOD_ID, "size_change_small"),
                    ModAbilityIcons.SIZE_CHANGE_SMALL,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    List::of,
                    -1, 0,
                    0.5f
            )
    );
    public static final GauntletAbility SIZE_CHANGE_BIG = GauntletAbilityRegistry.register(
            new SizeChangeAbility(
                    new Identifier(InfinityForge.MOD_ID, "size_change_big"),
                    ModAbilityIcons.SIZE_CHANGE_BIG,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    List::of,
                    -1, 0,
                    2.0f
            )
    );
    public static final GauntletAbility TURN_INTO_BUBBLES = GauntletAbilityRegistry.register(
            new TurnIntoBubblesAbility(
                    new Identifier(InfinityForge.MOD_ID, "turn_into_bubbles"),
                    ModAbilityIcons.TURN_INTO_BUBBLES,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    List::of,
                    100
            )
    );
    public static final GauntletAbility SPAWN_REAL_BLOCK = GauntletAbilityRegistry.register(
            new SpawnBlockAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_real_block"),
                    ModAbilityIcons.empty(),
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    () -> List.of(ModStones.POWER, ModStones.REALITY),
                    100, false
            )
    );
    public static final GauntletAbility SPAWN_FAKE_BLOCK = GauntletAbilityRegistry.register(
            new SpawnBlockAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_fake_block"),
                    ModAbilityIcons.empty(),
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    List::of,
                    100, true
            )
    );
    public static final GauntletAbility SPAWN_REAL_ITEM = GauntletAbilityRegistry.register(
            new SpawnItemAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_real_item"),
                    ModAbilityIcons.empty(),
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    () -> List.of(ModStones.POWER, ModStones.REALITY),
                    100, false
            )
    );
    public static final GauntletAbility SPAWN_FAKE_ITEM = GauntletAbilityRegistry.register(
            new SpawnItemAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_fake_item"),
                    ModAbilityIcons.empty(),
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.realityStone,
                    List::of,
                    100, true
            )
    );
    public static final GauntletAbility HEALTH = GauntletAbilityRegistry.register(
            new HealthAbility(
                    new Identifier(InfinityForge.MOD_ID, "health"),
                    ModAbilityIcons.HEALTH,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.soulStone,
                    20.0f
            )
    );
    public static final GauntletAbility HEALING = GauntletAbilityRegistry.register(
            new HealingAbility(
                    new Identifier(InfinityForge.MOD_ID, "healing"),
                    ModAbilityIcons.HEALING,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.soulStone,
                    0.5f, 10
            )
    );
    public static final GauntletAbility SATURATION = GauntletAbilityRegistry.register(
            new SaturationAbility(
                    new Identifier(InfinityForge.MOD_ID, "saturation"),
                    ModAbilityIcons.SATURATION,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.soulStone
            )
    );
    public static final GauntletAbility WATER_BREATHING = GauntletAbilityRegistry.register(
            new WaterBreathingAbility(
                    new Identifier(InfinityForge.MOD_ID, "water_breathing"),
                    ModAbilityIcons.WATER_BREATHING,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.soulStone
            )
    );
    public static final GauntletAbility POTION_RESISTANCE = GauntletAbilityRegistry.register(
            new PassiveAbility(
                    new Identifier(InfinityForge.MOD_ID, "potion_resistance"),
                    ModAbilityIcons.POTION_RESISTANCE,
                    () -> 0x7FFFFF,
                    List::of
            ) {}
    );
    public static final GauntletAbility KILL = GauntletAbilityRegistry.register(
            new KillAbility(
                    new Identifier(InfinityForge.MOD_ID, "kill"),
                    ModAbilityIcons.empty(),
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.soulStone,
                    () -> List.of(ModStones.POWER, ModStones.SPACE, ModStones.REALITY, ModStones.SOUL), 100
            )
    );
    public static final GauntletAbility SNAP = GauntletAbilityRegistry.register(
            new SnapAbility(
                    new Identifier(InfinityForge.MOD_ID, "snap"),
                    ModAbilityIcons.SNAP,
                    () -> 0x7FFFFF,
                    () -> ModStones.ALL_STONES,
                    20
            )
    );
    public static final GauntletAbility FLIGHT = GauntletAbilityRegistry.register(
            new FlightAbility(
                    new Identifier(InfinityForge.MOD_ID, "flight"),
                    ModAbilityIcons.FLIGHT,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.mindStone,
                    () -> List.of(ModStones.POWER, ModStones.MIND),
                    -1, 0
            )
    );
    public static final GauntletAbility TELEKINESIS = GauntletAbilityRegistry.register(
            new TelekinesisAbility(
                    new Identifier(InfinityForge.MOD_ID, "telekinesis"),
                    ModAbilityIcons.TELEKINESIS,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.mindStone,
                    List::of,
                    400, -1
            )
    );
    public static final GauntletAbility CHANGE_SNAP = GauntletAbilityRegistry.register(
            new ChangeSnapAbility(
                    new Identifier(InfinityForge.MOD_ID, "change_snap"),
                    ModAbilityIcons.empty(),
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.mindStone,
                    () -> ModStones.ALL_STONES,
                    0
            )
    );
    public static final GauntletAbility ADVANCE_TIME = GauntletAbilityRegistry.register(
            new AdvanceTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "advance_time"),
                    ModAbilityIcons.ADVANCE_TIME,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.timeStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility REWIND_TIME = GauntletAbilityRegistry.register(
            new RewindTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "rewind_time"),
                    ModAbilityIcons.REWIND_TIME,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.timeStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility STOP_TIME = GauntletAbilityRegistry.register(
            new StopTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "stop_time"),
                    ModAbilityIcons.STOP_TIME,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.timeStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility RANDOMISE_TIME = GauntletAbilityRegistry.register(
            new RandomiseTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "randomise_time"),
                    ModAbilityIcons.RANDOMISE_TIME,
                    () -> InfinityForgeClientConfig.get().abilityOutlineColors.timeStone,
                    List::of, 100
            )
    );
}

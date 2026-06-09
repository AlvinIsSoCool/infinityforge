package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.base.AttributeModifierAbility;
import net.alvin.infinityforge.infinity.abilities.base.LifecyclePassiveAbility;
import net.alvin.infinityforge.infinity.abilities.impl.mind.*;
import net.alvin.infinityforge.infinity.abilities.impl.reality.*;
import net.alvin.infinityforge.infinity.abilities.impl.soul.*;
import net.alvin.infinityforge.infinity.abilities.impl.space.*;
import net.alvin.infinityforge.infinity.abilities.impl.time.*;
import net.alvin.infinityforge.network.s2c.SyncStepHeightS2CPacket;
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
    static {
        AbilityIcon.setSheet(new Identifier(InfinityForge.MOD_ID, "textures/gui/ability_icons.png"));
    }

    private static final AbilityIcon.Allocator PASSIVE_ICONS =
            new AbilityIcon.Allocator(
                    new Identifier(InfinityForge.MOD_ID, "textures/gui/passive_ability_icons.png"),
                    0);

    public static final GauntletAbility KNOCKBACK_RESISTANCE = GauntletAbilityRegistry.register(
            new AttributeModifierAbility(
                    new Identifier(InfinityForge.MOD_ID, "knockback_resistance"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.knockback_resistance",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.powerStone,
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
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.speed",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.powerStone,
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
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.attack_speed",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.powerStone,
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
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.step_height",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.powerStone,
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
                    AbilityIcon.next(),
                    "abilities.infinityforge.teleport",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.spaceStone,
                    List::of, 100
            )
    );
    public static final GauntletAbility PORTAL = GauntletAbilityRegistry.register(
            new PortalAbility(
                    new Identifier(InfinityForge.MOD_ID, "portal"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.portal",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.spaceStone,
                    List::of, 0
            )
    );
    public static final GauntletAbility BLACKHOLE = GauntletAbilityRegistry.register(
            new BlackholeAbility(
                    new Identifier(InfinityForge.MOD_ID, "blackhole"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.blackhole",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.spaceStone,
                    List::of,
                    400, -1
            )
    );
    public static final GauntletAbility FORCEFIELD = GauntletAbilityRegistry.register(
            new ForcefieldAbility(
                    new Identifier(InfinityForge.MOD_ID, "forcefield"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.forcefield",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.spaceStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility PHASING = GauntletAbilityRegistry.register(
            new PhasingAbility(
                    new Identifier(InfinityForge.MOD_ID, "phasing"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.phasing",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.spaceStone,
                    () -> List.of(ModStones.SPACE, ModStones.MIND),
                    -1, 0
            )
    );
    public static final GauntletAbility WEATHER = GauntletAbilityRegistry.register(
            new WeatherAbility(
                    new Identifier(InfinityForge.MOD_ID, "weather"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.weather",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.realityStone,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100
            )
    );
    public static final GauntletAbility INVISIBILITY = GauntletAbilityRegistry.register(
            new InvisibilityAbility(
                    new Identifier(InfinityForge.MOD_ID, "invisibility"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.invisibility",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.realityStone,
                    List::of,
                    -1, 0
            )
    );
    public static final GauntletAbility SIZE_CHANGE_SMALL = GauntletAbilityRegistry.register(
            new SizeChangeAbility(
                    new Identifier(InfinityForge.MOD_ID, "size_change_small"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.size_change_small",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.realityStone,
                    List::of,
                    -1, 0,
                    0.5f
            )
    );
    public static final GauntletAbility SIZE_CHANGE_BIG = GauntletAbilityRegistry.register(
            new SizeChangeAbility(
                    new Identifier(InfinityForge.MOD_ID, "size_change_big"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.size_change_big",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.realityStone,
                    List::of,
                    -1, 0,
                    2.0f
            )
    );
    public static final GauntletAbility SPAWN_REAL_BLOCK = GauntletAbilityRegistry.register(
            new SpawnBlockAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_real_block"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.spawn_real_block",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.realityStone,
                    () -> List.of(ModStones.POWER, ModStones.REALITY), 100,
                    false
            )
    );
    public static final GauntletAbility SPAWN_FAKE_ITEM = GauntletAbilityRegistry.register(
            new SpawnItemAbility(
                    new Identifier(InfinityForge.MOD_ID, "spawn_fake_item"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.spawn_fake_item",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.realityStone,
                    () -> List.of(ModStones.POWER, ModStones.REALITY),
                    100, true
            )
    );
    public static final GauntletAbility KILL = GauntletAbilityRegistry.register(
            new KillAbility(
                    new Identifier(InfinityForge.MOD_ID, "kill"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.kill",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.soulStone,
                    () -> List.of(ModStones.POWER, ModStones.SOUL), 100
            )
    );
    public static final GauntletAbility HEALTH = GauntletAbilityRegistry.register(
            new HealthAbility(
                    new Identifier(InfinityForge.MOD_ID, "health"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.health",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.soulStone,
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
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.soulStone
            )
    );
    public static final GauntletAbility SATURATION = GauntletAbilityRegistry.register(
            new SaturationAbility(
                    new Identifier(InfinityForge.MOD_ID, "saturation"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.saturation",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.soulStone
            )
    );
    public static final GauntletAbility WATER_BREATHING = GauntletAbilityRegistry.register(
            new WaterBreathingAbility(
                    new Identifier(InfinityForge.MOD_ID, "water_breathing"),
                    PASSIVE_ICONS.next(),
                    "abilities.infinityforge.water_breathing",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.soulStone
            )
    );
    public static final GauntletAbility SNAP = GauntletAbilityRegistry.register(
            new SnapAbility(
                    new Identifier(InfinityForge.MOD_ID, "snap"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.snap",
                    () -> 0x7FFFFF,
                    () -> ModStones.ALL_STONES,
                    20
            )
    );
    public static final GauntletAbility FLIGHT = GauntletAbilityRegistry.register(
            new FlightAbility(
                    new Identifier(InfinityForge.MOD_ID, "flight"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.flight",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.mindStone,
                    () -> List.of(ModStones.POWER, ModStones.MIND),
                    -1, 0
            )
    );
    public static final GauntletAbility CHANGE_SNAP = GauntletAbilityRegistry.register(
            new ChangeSnapAbility(
                    new Identifier(InfinityForge.MOD_ID, "change_snap"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.change_snap",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.mindStone,
                    () -> ModStones.ALL_STONES,
                    0
            )
    );
    public static final GauntletAbility ADVANCE_TIME = GauntletAbilityRegistry.register(
            new AdvanceTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "advance_time"),
                    AbilityIcon.missingno(),
                    "abilities.infinityforge.advance_time",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.timeStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility REWIND_TIME = GauntletAbilityRegistry.register(
            new RewindTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "rewind_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.rewind_time",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.timeStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility STOP_TIME = GauntletAbilityRegistry.register(
            new StopTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "stop_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.stop_time",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.timeStone,
                    List::of,
                    400, -4
            )
    );
    public static final GauntletAbility RANDOMISE_TIME = GauntletAbilityRegistry.register(
            new RandomiseTimeAbility(
                    new Identifier(InfinityForge.MOD_ID, "randomise_time"),
                    AbilityIcon.next(),
                    "abilities.infinityforge.randomise_time",
                    () -> InfinityForgeConfig.get().colorOptions.abilityOutlineColors.timeStone,
                    List::of, 100
            )
    );
}

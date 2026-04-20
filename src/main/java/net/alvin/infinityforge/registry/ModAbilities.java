package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.*;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.abilities.registry.GauntletAbilityRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class ModAbilities {
    public static final GauntletAbility HEALING = GauntletAbilityRegistry.register(new AbilityHealing());
    public static final GauntletAbility WEATHER = GauntletAbilityRegistry.register(new AbilityWeather());
    public static final GauntletAbility WEATHER_TOGGLE = GauntletAbilityRegistry.register(new AbilityWeatherToggle());
    public static final GauntletAbility WEATHER_HELD = GauntletAbilityRegistry.register(new AbilityWeatherHeld());

    public static final GauntletAbility AB_1 = GauntletAbilityRegistry.register(new ActiveAbility(
            new Identifier(InfinityForge.MOD_ID, "ab_1"),
            new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/null.png"),
            Text.translatable("abilities." + InfinityForge.MOD_ID + ".ab_1").getString(),
            0xFF000000, 0) {
        @Override
        public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 600, 255));
        }
    });
    public static final GauntletAbility AB_2 = GauntletAbilityRegistry.register(new ActiveAbility(
            new Identifier(InfinityForge.MOD_ID, "ab_2"),
            new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/null.png"),
            Text.translatable("abilities." + InfinityForge.MOD_ID + ".ab_2").getString(),
            0xFF000000, 0) {
        @Override
        public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 600, 255));
        }
    });
    public static final GauntletAbility AB_3 = GauntletAbilityRegistry.register(new ActiveAbility(
            new Identifier(InfinityForge.MOD_ID, "ab_3"),
            new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/null.png"),
            Text.translatable("abilities." + InfinityForge.MOD_ID + ".ab_3").getString(),
            0xFF000000, 0) {
        @Override
        public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 600, 255));
        }
    });
    public static final GauntletAbility AB_4 = GauntletAbilityRegistry.register(new ActiveAbility(
            new Identifier(InfinityForge.MOD_ID, "ab_4"),
            new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/null.png"),
            Text.translatable("abilities." + InfinityForge.MOD_ID + ".ab_4").getString(),
            0xFF000000, 0) {
        @Override
        public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 600, 255));
        }
    });

}

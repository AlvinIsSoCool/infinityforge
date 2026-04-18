package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.registries.GauntletAbilityRegistry;

public class ModAbilities {
    public static final GauntletAbility HEALING = GauntletAbilityRegistry.register(new AbilityHealing());
    public static final GauntletAbility WEATHER = GauntletAbilityRegistry.register(new AbilityWeather());
}

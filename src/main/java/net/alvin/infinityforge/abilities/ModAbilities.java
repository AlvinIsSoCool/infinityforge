package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.registries.GauntletAbilityRegistry;

public class ModAbilities {
    public static final GauntletAbility TEST = GauntletAbilityRegistry.register(new TestGauntletAbility());
    public static final GauntletAbility TEST2 = GauntletAbilityRegistry.register(new TestGauntletAbility2());
}

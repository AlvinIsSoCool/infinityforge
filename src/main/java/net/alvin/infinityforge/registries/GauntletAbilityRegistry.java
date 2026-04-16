package net.alvin.infinityforge.registries;

import net.alvin.infinityforge.abilities.GauntletAbility;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class GauntletAbilityRegistry {
    private static final Map<Identifier, GauntletAbility> ABILITY_REGISTRY = new HashMap<>();

    public static GauntletAbility register(GauntletAbility ability) {
        ABILITY_REGISTRY.put(ability.getId(), ability);
        return ability;
    }

    public static GauntletAbility get(Identifier id) {
        return ABILITY_REGISTRY.get(id);
    }
}

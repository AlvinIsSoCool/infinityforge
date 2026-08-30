package net.alvin.infinityforge.entity.effect;

import net.minecraft.entity.effect.StatusEffectInstance;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class HarmfulEffectsBypass {
    private static final Set<StatusEffectInstance> EXEMPT =
            Collections.newSetFromMap(new IdentityHashMap<>());

    public static StatusEffectInstance exempt(StatusEffectInstance instance) {
        EXEMPT.add(instance);
        return instance;
    }

    public static boolean isExempt(StatusEffectInstance instance) {
        return EXEMPT.remove(instance);
    }
}

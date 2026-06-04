package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.effect.HealthDrainStatusEffect;
import net.alvin.infinityforge.effect.MovementLockedStatusEffect;
import net.alvin.infinityforge.effect.ScrollLockedStatusEffect;
import net.alvin.infinityforge.effect.SnapStatusEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModStatusEffects {
    public static final StatusEffect SNAP_EFFECT = register(
            new Identifier(InfinityForge.MOD_ID, "snap"),
            new SnapStatusEffect(StatusEffectCategory.HARMFUL, 0xFF7F50)
    );

    public static final StatusEffect MOVEMENT_LOCKED_EFFECT = register(
            new Identifier(InfinityForge.MOD_ID, "movement_locked"),
            new MovementLockedStatusEffect(StatusEffectCategory.HARMFUL, 0)
    );

    public static final StatusEffect SCROLL_LOCKED_EFFECT = register(
            new Identifier(InfinityForge.MOD_ID, "scroll_locked"),
            new ScrollLockedStatusEffect(StatusEffectCategory.HARMFUL, 0)
    );

    public static final StatusEffect HEALTH_DRAIN_EFFECT = register(
            new Identifier(InfinityForge.MOD_ID, "health_drain"),
            new HealthDrainStatusEffect(StatusEffectCategory.HARMFUL, 0)
    );

    public static StatusEffect register(Identifier id, StatusEffect effect) {
        return Registry.register(Registries.STATUS_EFFECT, id, effect);
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Status Effects for: {}", InfinityForge.MOD_ID);
    }
}

package net.alvin.infinityforge.entity.effect;

import net.alvin.infinityforge.registry.ModDamageSources;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class HealthDrainStatusEffect extends StatusEffect {
    public static final UUID HEALTH_DRAIN_UUID = UUID.fromString("a39b62fe-6778-4208-9b6d-ec80409cd4aa");

    public HealthDrainStatusEffect(StatusEffectCategory category, int color) { super(category, color); }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.getWorld().isClient) return;

        long worldTime = entity.getWorld().getTime();
        int offset = Math.abs(entity.getUuid().hashCode() % 40);
        if ((worldTime + offset) % 40 != 0) return;

        EntityAttributeInstance attr = entity.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH);
        if (attr == null) return;

        double currentMax = attr.getValue();
        if (currentMax <= 2.0) {
            InfinityStoneEventHandler.applyDamageInfinity(entity,
                    ModDamageSources.healthDrain(entity.getWorld()),
                    false);
            return;
        }

        double currentReduction = attr.getModifiers()
                .stream()
                .filter(m -> m.getId().equals(HEALTH_DRAIN_UUID))
                .findFirst()
                .map(EntityAttributeModifier::getValue)
                .orElse(0.0);
        double newReduction = currentReduction - 2.0;

        attr.removeModifier(HEALTH_DRAIN_UUID);
        EntityAttributeModifier modifier = new EntityAttributeModifier(
                HEALTH_DRAIN_UUID,
                "Health Drain",
                newReduction,
                EntityAttributeModifier.Operation.ADDITION
        );
        attr.addTemporaryModifier(modifier);

        double newMax = entity.getMaxHealth();
        if (entity.getHealth() > newMax) {
            entity.setHealth((float) newMax);
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) { return true; }
}

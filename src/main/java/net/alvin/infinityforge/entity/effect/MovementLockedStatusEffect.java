package net.alvin.infinityforge.entity.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class MovementLockedStatusEffect extends StatusEffect {
    private static final UUID MOVEMENT_LOCKED_UUID = UUID.fromString("9f27ad46-bdd1-416d-841d-815d5406d707");

    public MovementLockedStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance attr = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;

        EntityAttributeModifier modifier = new EntityAttributeModifier(
                MOVEMENT_LOCKED_UUID,
                "Movement Lock",
                -1f,
                EntityAttributeModifier.Operation.MULTIPLY_BASE
        );
        attr.addTemporaryModifier(modifier);
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        EntityAttributeInstance attr = entity.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attr == null) return;
        attr.removeModifier(MOVEMENT_LOCKED_UUID);
    }
}

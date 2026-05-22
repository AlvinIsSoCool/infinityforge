package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.ext.AttributeModifierAbility;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public class HealthAbility extends AttributeModifierAbility {

    public HealthAbility(Identifier id, AbilityIcon icon,
                         String key, int color,
                         Map<EntityAttribute, EntityAttributeModifier> modifiers) {
        super(id, icon, key, color, modifiers);
    }

    @Override
    public void onRemove(ServerPlayerEntity player, Identifier abilityId) {
        super.onRemove(player, abilityId);

        float maxHealth = player.getMaxHealth();
        if (maxHealth < player.getHealth()) {
            player.setHealth(maxHealth);
            player.damage(player.getWorld().getDamageSources().generic(), 1.0f);
        }
    }
}
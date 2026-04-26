package net.alvin.infinityforge.abilities.impl.soul;

import net.alvin.infinityforge.abilities.ext.AttributeModifierAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class HealthAbility extends AttributeModifierAbility {

    public HealthAbility(Identifier id, Identifier icon,
                         String key, int color,
                         Supplier<List<InfinityStoneType>> requiredStones,
                         Map<EntityAttribute, EntityAttributeModifier> modifiers) {
        super(id, icon, key, color, requiredStones, modifiers);
    }

    @Override
    public void onRemove(ServerPlayerEntity player) {
        super.onRemove(player);

        float maxHealth = player.getMaxHealth();
        if (maxHealth < player.getHealth()) {
            player.setHealth(maxHealth);
            player.damage(player.getWorld().getDamageSources().generic(), 1.0f);
        }
    }
}
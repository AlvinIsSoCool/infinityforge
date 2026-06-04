package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.AttributeModifierAbility;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class HealthAbility extends AttributeModifierAbility {

    public HealthAbility(Identifier id, AbilityIcon icon,
                         String key, Supplier<Integer> color,
                         Map<EntityAttribute, EntityAttributeModifier> modifiers) {
        super(id, icon, key, color, List::of, modifiers);
    }

    @Override
    public void onEnd(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        super.onEnd(world, player, activeStones);

        float maxHealth = player.getMaxHealth();
        if (maxHealth < player.getHealth()) {
            player.setHealth(maxHealth);
            player.damage(player.getWorld().getDamageSources().generic(), 1.0f);
        }
    }
}
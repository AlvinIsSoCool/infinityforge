package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.AttributeModifierAbility;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class HealthAbility extends AttributeModifierAbility {

    public HealthAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color, float amount) {
        super(id, icon, color, List::of, Map.of(
                EntityAttributes.GENERIC_MAX_HEALTH,
                new EntityAttributeModifier(
                        UUID.randomUUID(),
                        "Health",
                        amount,
                        EntityAttributeModifier.Operation.ADDITION
                )
        ));
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
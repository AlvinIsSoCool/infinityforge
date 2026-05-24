package net.alvin.infinityforge.infinity.abilities.ext;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.server.state.GauntletAttributeState;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AttributeModifierAbility extends PassiveAbility {
    private final Map<EntityAttribute, EntityAttributeModifier> modifiers;

    public AttributeModifierAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color,
                                    Supplier<List<InfinityStoneType>> requiredStones,
                                    Map<EntityAttribute, EntityAttributeModifier> modifiers) {
        super(id, icon, key, color, requiredStones);
        this.modifiers = modifiers;
    }

    public AttributeModifierAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color,
                                    Map<EntityAttribute, EntityAttributeModifier> modifiers) {
        super(id, icon, key, color);
        this.modifiers = modifiers;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player,
                       List<InfinityStoneType> activeStones) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : modifiers.entrySet()) {
            EntityAttributeInstance instance = player.getAttributeInstance(entry.getKey());
            if (instance != null && !instance.hasModifier(entry.getValue())) {
                instance.addTemporaryModifier(entry.getValue());
                GauntletAttributeState.markActive(player, getId());
            }
        }
    }

    public void onRemove(ServerPlayerEntity player, Identifier abilityId) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : modifiers.entrySet()) {
            EntityAttributeInstance instance = player.getAttributeInstance(entry.getKey());
            if (instance != null && instance.hasModifier(entry.getValue())) {
                instance.removeModifier(entry.getValue());
                GauntletAttributeState.markInactive(player, abilityId);
            }
        }
    }
}
package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AttributeModifierAbility extends LifecyclePassiveAbility {
    private final Map<EntityAttribute, EntityAttributeModifier> modifiers;

    public AttributeModifierAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color,
                                    Supplier<List<InfinityStoneType>> requiredStones,
                                    Map<EntityAttribute, EntityAttributeModifier> modifiers) {
        super(id, icon, color, requiredStones);
        this.modifiers = modifiers;
    }

    /**
     * The function that runs when this ability is first added.
     * All logic contained within should be server-side.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    @Override
    public void onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : modifiers.entrySet()) {
            EntityAttributeInstance instance = player.getAttributeInstance(entry.getKey());
            if (instance != null && !instance.hasModifier(entry.getValue()))
                instance.addTemporaryModifier(entry.getValue());
        }
    }

    /**
     * The function that runs when this ability is removed.
     * All logic contained within should be server-side.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    @Override
    public void onEnd(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : modifiers.entrySet()) {
            EntityAttributeInstance instance = player.getAttributeInstance(entry.getKey());
            if (instance != null && instance.hasModifier(entry.getValue()))
                instance.removeModifier(entry.getValue());
        }
    }
}
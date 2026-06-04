package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class LifecyclePassiveAbility extends PassiveAbility {
    private final Set<PlayerEntity> ACTIVE_PASSIVES = Collections.newSetFromMap(new IdentityHashMap<>());

    public LifecyclePassiveAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones) {
        super(id, icon, key, color, requiredStones);
    }

    /**
     * The function that runs while this ability is active.
     * A special override of the super method onTick.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     * @implNote This function is not to be overriden.
     *           If overriden from a subclass, this method has to be called first.
     *           Any special requirements should override onStart and onPassiveTick instead.
     */
    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (ACTIVE_PASSIVES.add(player)) {
            onStart(world, player, activeStones);
        }
        onPassiveTick(world, player, activeStones);
    }

    /**
     * The function that runs when this ability is first added.
     * All logic contained within should be server-side.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public void onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    /**
     * The function that runs while this ability is active.
     * Run from onTick override in this class. All logic contained within should
     * be server-side.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    @SuppressWarnings("EmptyMethod")
    public void onPassiveTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    /**
     * The function that runs when this ability is removed.
     * Called from triggerEnd function. All logic contained within should
     * be server-side.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public void onEnd(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    /**
     * The function that runs when this ability is about to be removed.
     * This is the function that calls onEnd for every LifecyclePassiveAbility subclasses.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     * @implNote This function is not to be overriden.
     *           If overriden from a subclass, this method has to be called first.
     *           Any special cleanup requirements should override onEnd instead.
     */
    public void triggerEnd(ServerWorld world, ServerPlayerEntity player,
                               List<InfinityStoneType> activeStones) {
        if (ACTIVE_PASSIVES.remove(player)) {
            onEnd(world, player, activeStones);
        }
    }
}

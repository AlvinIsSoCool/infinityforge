package net.alvin.infinityforge.infinity.abilities.ext;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.network.s2c.SyncAbilityDynamicIconS2CPacket;
import net.alvin.infinityforge.server.state.StatefulAbilityState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public abstract class StatefulAbility<T> extends ActiveAbility {
    public StatefulAbility(Identifier id, AbilityIcon icon,
                           String key, Supplier<Integer> color,
                           Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    public StatefulAbility(Identifier id, AbilityIcon icon,
                           String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones) {
        super(id, icon, key, color, requiredStones);
    }

    public StatefulAbility(Identifier id, AbilityIcon icon,
                           String key, Supplier<Integer> color) {
        super(id, icon, key, color);
    }

    /**
     * Abstract function that all inheritents must define according to what state class
     * the ability is storing.
     * @return The state class type for type casting safety.
     */
    protected abstract Class<T> getStateType();

    /**
     * Retrives the state that the ability is storing, that is keyed on the server player.
     * @param player The player that the state is keyed on (This is the player on the server)
     * @return The state that the ability is storing.
     */
    public T getState(PlayerEntity player) { return StatefulAbilityState.get(player, getId(), getStateType()); }

    /**
     * Set the state that the ability should store
     * @param player The player that the state will be keyed on (This is the player on the server)
     * @param state The state the ability will store.
     */
    protected void setState(PlayerEntity player, T state) {
        StatefulAbilityState.set(player, getId(), state);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            ItemStack iconStack = getStateIconStack(state);
            if (iconStack != null && !iconStack.isEmpty()) {
                ServerPlayNetworking.send(serverPlayer,
                        new SyncAbilityDynamicIconS2CPacket(getId(), iconStack));
            } else {
                ServerPlayNetworking.send(serverPlayer,
                        new SyncAbilityDynamicIconS2CPacket(getId(), ItemStack.EMPTY));
            }
        }
    }
    protected void clearState(PlayerEntity player) { StatefulAbilityState.clear(player, getId()); }

    /**
     * Return an itemstack that represents an icon for the current state.
     * Default returns null (no dynamic icon).
     * @param state The state on the server side that can be used to derive the icon.
     * @return A valid itemstack of the state from the required registry.
     * @implSpec If the state given is not something that contains a minecraft registry identifier,
     *       consider returning null. Crashes are likely, if used for things other than valid
     *       registered things like blocks, items, itemstacks (the item that it contains, basically), etc.
     */
    protected ItemStack getStateIconStack(T state) {
        return null;
    }
}

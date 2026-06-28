package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.network.s2c.SyncAbilityDynamicIconS2CPacket;
import net.alvin.infinityforge.server.state.GauntletAbilityStates;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

/**
 * The interface for storing states for abilities.
 * @param <T> The state type.
 */
public interface AbilityState<T> {
    /**
     * All interfacing classes must return the class of the state type.
     * @return The class of the state type.
     */
    Class<T> getType();

    /**
     * Retrieves the state stored by the ability, keyed on the user.
     * @param player The user of the ability who is keyed as the
     *               owner of the state.
     * @return The stored ability state.
     */
    default T getState(PlayerEntity player) {
        Identifier id = ((GauntletAbility) this).getId();
        return GauntletAbilityStates.get(player, id, getType());
    }

    /**
     * Sets the state to be stored for the ability, keyed on the user.
     * @param player The user of the ability who will be keyed as the
     *               owner of the state.
     * @param state The state to set.
     * @implNote Please note that this method also syncs dynamic icons, if implemented.
     *           An unchecked cast is used, so when implementing both interfaces,
     *           the types must be the same. In a case where the types are different,
     *           the method will silently pollute the heap and cause instability.
     *           Also, if the state is something that cannot be represented as an
     *           {@link ItemStack}, do not implement {@link AbilityDynamicIcon} with a random type.
     */
    default void setState(PlayerEntity player, T state) {
        Identifier id = ((GauntletAbility) this).getId();
        GauntletAbilityStates.set(player, id, state);

        if (player instanceof ServerPlayerEntity serverPlayer
                && this instanceof AbilityDynamicIcon<?> icon) {
            @SuppressWarnings("unchecked")
            ItemStack iconStack = ((AbilityDynamicIcon<T>) icon).getDynamicIcon(state);
            ServerPlayNetworking.send(serverPlayer,
                    new SyncAbilityDynamicIconS2CPacket(id, iconStack));
        }
    }
}

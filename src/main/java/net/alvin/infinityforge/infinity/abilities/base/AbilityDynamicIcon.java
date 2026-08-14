package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.abilities.icon.ModAbilityIcons;
import net.minecraft.item.ItemStack;

/**
 * The interface providing dynamic icons.
 * An {@link ItemStack} represents a dynamic icon.
 * @param <T> The type given when {@link AbilityState} is implemented
 *            alongside this interface. Must match what is provided to {@link AbilityState}
 */
public interface AbilityDynamicIcon<T> {
    /**
     * Retrives the {@link ItemStack} that will represent the dynamic icon.
     * All implementing classes must override this to provide functionality.
     * @param state If {@link AbilityState} is implemented alongside this interface,
     *              the state that is used to retrieve the dynamic icon.
     * @return The {@link ItemStack} that will represent the dynamic icon.
     * @implNote Please note that state types must match when co-implemented with {@link AbilityState}.
     *           Also, if no dynamic icon is to be provided other than the default implementation
     *           ({@link ItemStack#EMPTY}), please do not use this interface.
     *           Use the static icon functions for this like {@link ModAbilityIcons#empty()}
     *           If no state is used and dynamic icons are required feel free to use
     *           {@link Void} as the type.
     */
    default ItemStack getDynamicIcon(T state) { return ItemStack.EMPTY; }
}

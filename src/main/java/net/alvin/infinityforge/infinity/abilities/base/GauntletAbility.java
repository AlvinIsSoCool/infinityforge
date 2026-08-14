package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.List;

/**
 * The sealed interface that all permitted gauntlet ability types implement.
 */
public sealed interface GauntletAbility permits ActiveAbility, HeldAbility, PassiveAbility, ToggleAbility {
    Identifier getId();
    AbilityIcon getIcon();
    String getName();
    int getARGBColor();
    int getRGBColor();

    /**
     * Called when checking whether the player has the required stones
     * equipped in their gauntlet. Should be overriden by child classes.
     * @param activeStones The list of infinity stone types in the gauntlet at the time of
     *                     this function call.
     * @return Whether the required stones are present.
     * @implNote If requiredStones for a child ability is empty, meetsCondition will return true, due to
     *           {@link AbstractCollection#containsAll(Collection)} check in the child classes.
     *           Explicitly check whether requiredStones list is empty to handle this special case.
     */
    default boolean meetsCondition(List<InfinityStoneType> activeStones) { return true; }

    /**
     * Finds the ability with the given Identifier from the provided list.
     * @param abilities The list of abilities from which the ability should be found.
     * @param id The Identifier of the ability that is to be found
     * @return The ability that is found, or {@code null} if the ability is not found
     * @param <T> Any class that extends GauntletAbility.
     * @implNote It seems much faster to just iterate through the GauntletAbilityRegistry with the
     *           ability id for certain situations. This method should only be used when an
     *           abilities list is provided by performant code, instead of manually calling for the
     *           list and using this method on it. An exception is where an ability has to be filtered from
     *           abilities available to the gauntlet.
     */
    @Nullable
    static <T extends GauntletAbility> T findAbility(List<T> abilities, Identifier id) {
        for (T ability : abilities)
            if (ability.getId().equals(id))
                return ability;
        return null;
    }
}

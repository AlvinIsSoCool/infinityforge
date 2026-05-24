package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * The sealed interface that all the gauntlet ability types implement.
 */
public sealed interface GauntletAbility permits ActiveAbility, HeldAbility, PassiveAbility, ToggleAbility {
    Identifier getId();
    AbilityIcon getIcon();
    String getName();
    int getColor();

    default boolean meetsCondition(List<InfinityStoneType> activeStones) {
        return true;
    }
}

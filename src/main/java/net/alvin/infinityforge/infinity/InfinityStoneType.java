package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.base.StoneHoldAbility;
import net.alvin.infinityforge.infinity.abilities.base.StoneUseAbility;

import java.util.List;
import java.util.function.Supplier;

/**
 * The record class that holds details about the infinity stone type.
 * Registering a new infinity stone would require this particular record to be initialized
 * with all the params given below.
 * @param useAbility Provides a function that runs on right-clicking the infinity stone.
 * @param holdAbility Provides a function that runs every tick when the infinity stone is held
 * @param gauntletAbilities Provides a list of abilities to be added to the infinity gauntlet
 *                          when the infinity stone is equipped.
 * @param baseColorSupplier Provides a supplier of the base color of the infinity stone.
 * @param glintColorSupplier Provides a supplier of the color of the infinity stone glint effect.
 *                           A brighter version of the baseColor is recommended.
 */
public record InfinityStoneType(
        StoneUseAbility useAbility, StoneHoldAbility holdAbility, List<GauntletAbility> gauntletAbilities,
        Supplier<Integer> baseColorSupplier, Supplier<Integer> glintColorSupplier
) {
    public int getBaseColor() {
        return baseColorSupplier.get();
    }

    public int getGlintColor() {
        return glintColorSupplier.get();
    }
}

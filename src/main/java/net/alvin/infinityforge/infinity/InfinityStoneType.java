package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.abilities.base.GauntletAbility;

import java.util.List;

/**
 * The record class that holds details about the infinity stone type.
 * Registering a new infinity stone would require this particular record to be initialized
 * with all the params given below.
 * @param useAbility Provides a function that runs on right-clicking the infinity stone.
 * @param holdAbility Provides a function that runs every tick when the infinity stone is held
 * @param gauntletAbilities Provides a list of abilities to be added to the infinity gauntlet
 *                          when the infinity stone is equipped.
 * @param baseColor Provides the base color of the infinity stone.
 * @param glintColor Provides the color of infinity stone glint effect.
 *                   A brighter version of the baseColor is recommended.
 */
public record InfinityStoneType(
        UseAbility useAbility, HoldAbility holdAbility, List<GauntletAbility> gauntletAbilities,
        int baseColor, int glintColor
) {}

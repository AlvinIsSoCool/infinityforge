package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.abilities.base.GauntletAbility;

import java.util.List;

public record InfinityStoneType(
        UseAbility useAbility, HoldAbility holdAbility, List<GauntletAbility> gauntletAbilities,
        int baseColor, int glintColor
) {}

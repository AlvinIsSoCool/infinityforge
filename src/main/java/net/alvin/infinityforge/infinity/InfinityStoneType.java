package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.abilities.HoldAbility;
import net.alvin.infinityforge.abilities.UseAbility;

import java.util.List;

public record InfinityStoneType(
        UseAbility useAbility, HoldAbility holdAbility, List<GauntletAbility> gauntletAbilities,
        int baseColor, int glintColor
) {}

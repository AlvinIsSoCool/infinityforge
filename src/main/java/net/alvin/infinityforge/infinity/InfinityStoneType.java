package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.abilities.HoldAbility;
import net.alvin.infinityforge.abilities.UseAbility;

import java.util.List;

public class InfinityStoneType {
    private final UseAbility useAbility;
    private final HoldAbility holdAbility;
    private final List<GauntletAbility> gauntletAbilities;

    public InfinityStoneType(UseAbility useAbility, HoldAbility holdAbility, List<GauntletAbility> gauntletAbilities) {
        this.useAbility = useAbility;
        this.holdAbility = holdAbility;
        this.gauntletAbilities = gauntletAbilities;
    }

    public UseAbility getUseAbility() {
        return useAbility;
    }

    public HoldAbility getHoldAbility() {
        return holdAbility;
    }

    public List<GauntletAbility> getGauntletAbilities() {
        return gauntletAbilities;
    }
}

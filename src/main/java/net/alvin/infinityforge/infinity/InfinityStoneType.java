package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.abilities.HoldAbility;
import net.alvin.infinityforge.abilities.UseAbility;

import java.util.List;

public class InfinityStoneType {
    private final UseAbility useAbility;
    private final HoldAbility holdAbility;
    private final List<GauntletAbility> gauntletAbilities;
    private final int baseColor;
    private final int glintColor;

    public InfinityStoneType(UseAbility useAbility, HoldAbility holdAbility, List<GauntletAbility> gauntletAbilities, int baseColor, int glintColor) {
        this.useAbility = useAbility;
        this.holdAbility = holdAbility;
        this.gauntletAbilities = gauntletAbilities;
        this.baseColor = baseColor;
        this.glintColor = glintColor;
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

    public int getBaseColor() {
        return baseColor;
    }

    public int getGlintColor() {
        return glintColor;
    }
}

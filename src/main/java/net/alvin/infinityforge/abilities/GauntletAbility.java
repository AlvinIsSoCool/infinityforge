package net.alvin.infinityforge.abilities;

import net.minecraft.util.Identifier;

public sealed interface GauntletAbility permits ActiveAbility, HeldAbility, PassiveAbility, ToggleAbility
{
    Identifier getId();
    Identifier getIcon();
    String getName();
    int getColor();
    int getCooldownTicks();
    int getMaxChargeTicks();
    int getRefillRateTicks();
}

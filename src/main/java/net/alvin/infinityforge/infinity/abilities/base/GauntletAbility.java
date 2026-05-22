package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The sealed interface that all the gauntlet ability types implement.
 */
public sealed interface GauntletAbility permits ActiveAbility, HeldAbility, PassiveAbility, ToggleAbility {
    Identifier DEFAULT_ICON_LOCATION = new Identifier(InfinityForge.MOD_ID, "textures/gui/ability_icons.png");
    Identifier DEFAULT_PASSIVE_ICON_LOCATION = new Identifier(InfinityForge.MOD_ID, "textures/gui/passive_ability_icons.png");

    static Identifier iconLocationOrDefault(@Nullable Identifier iconLocation) {
        return iconLocation != null ? iconLocation : DEFAULT_ICON_LOCATION;
    }

    static Identifier passiveIconLocationOrDefault(@Nullable Identifier iconLocation) {
        return iconLocation != null ? iconLocation : DEFAULT_PASSIVE_ICON_LOCATION;
    }

    Identifier getId();
    AbilityIcon getIcon();
    String getName();
    int getColor();

    default boolean meetsCondition(List<InfinityStoneType> activeStones) {
        return true;
    }
}

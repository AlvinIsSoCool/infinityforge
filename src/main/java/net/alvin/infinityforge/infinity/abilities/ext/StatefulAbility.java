package net.alvin.infinityforge.infinity.abilities.ext;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.server.state.StatefulAbilityState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public abstract class StatefulAbility<T> extends ActiveAbility {
    public StatefulAbility(Identifier id, AbilityIcon icon,
                           String key, int color,
                           Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    public StatefulAbility(Identifier id, AbilityIcon icon,
                           String key, int color, Supplier<List<InfinityStoneType>> requiredStones) {
        super(id, icon, key, color, requiredStones);
    }

    public StatefulAbility(Identifier id, AbilityIcon icon,
                           String key, int color) {
        super(id, icon, key, color);
    }

    protected T getState(PlayerEntity player) { return StatefulAbilityState.get(player, getId()); }
    protected void setState(PlayerEntity player, T state) { StatefulAbilityState.set(player, getId(), state); }
    protected void clearState(PlayerEntity player) { StatefulAbilityState.set(player, getId(), null); }
}

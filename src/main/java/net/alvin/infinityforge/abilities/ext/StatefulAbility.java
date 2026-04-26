package net.alvin.infinityforge.abilities.ext;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.server.state.StatefulAbilityState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public abstract class StatefulAbility<T> extends ActiveAbility {
    public StatefulAbility(Identifier id, Identifier icon,
                           String key, int color,
                           Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    protected T getState(PlayerEntity player) {
        return StatefulAbilityState.get(player, getId());
    }

    protected void setState(PlayerEntity player, T state) {
        StatefulAbilityState.set(player, getId(), state);
    }

    protected void clearState(PlayerEntity player) {
        StatefulAbilityState.set(player, getId(), null);
    }
}

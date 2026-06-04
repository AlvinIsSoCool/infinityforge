package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.network.s2c.SyncAbilityDynamicIconS2CPacket;
import net.alvin.infinityforge.server.state.StatefulAbilityState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public interface AbilityState<T> {
    Class<T> getType();

    default T getState(PlayerEntity player) {
        Identifier id = ((GauntletAbility) this).getId();
        return StatefulAbilityState.get(player, id, getType());
    }

    default void setState(PlayerEntity player, T state) {
        Identifier id = ((GauntletAbility) this).getId();
        StatefulAbilityState.set(player, id, state);

        if (player instanceof ServerPlayerEntity serverPlayer) {
            ItemStack icon = getDynamicIcon(state);
            ServerPlayNetworking.send(serverPlayer,
                    new SyncAbilityDynamicIconS2CPacket(id,
                            icon != null ? icon : ItemStack.EMPTY));
        }
    }

    default ItemStack getDynamicIcon(T state) { return null; }
}

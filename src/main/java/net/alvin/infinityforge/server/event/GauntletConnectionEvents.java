package net.alvin.infinityforge.server.event;

import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.abilities.ext.AttributeModifierAbility;
import net.alvin.infinityforge.item.FakeItem;
import net.alvin.infinityforge.server.state.*;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.GauntletAbilityRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GauntletConnectionEvents {
    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register(
                (handler, server) -> cleanupPlayerAll(handler.getPlayer()));
    }

    public static void cleanupPlayerAll(ServerPlayerEntity player) {
        player.getInventory().remove(
                stack -> stack.getItem() instanceof FakeItem,
                Integer.MAX_VALUE,
                player.getInventory()
        );
        ItemStack stack = GauntletChargeState.getLastKnownStack(player);
        if (stack != null) cleanupPlayer(player, stack);
        GauntletChargeState.clearPlayer(player);
        StatefulAbilityState.clear(player);
        PendingInfinityItemPickups.clear(player);
    }

    public static void cleanupPlayer(ServerPlayerEntity player, ItemStack stack) {
        ServerWorld world = (ServerWorld) player.getWorld();
        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);

        UUID gauntletId = InfinityGauntletItem.getOrCreateGauntletId(stack);
        InfinityGauntletItem.saveToStack(stack, gauntletId, world.getTime());
        GauntletChargeState.clear(gauntletId);
        GauntletCooldownState.clear(gauntletId);

        for (Identifier id : new HashSet<>(GauntletToggleState.getActive(player))) {
            GauntletAbility ability = GauntletAbilityRegistry.get(id);
            if (ability instanceof ToggleAbility t)
                t.onDisable(world, player, activeStones);
        }

        for (Identifier id : new HashSet<>(GauntletHeldState.getHeld(player))) {
            GauntletAbility ability = GauntletAbilityRegistry.get(id);
            if (ability instanceof HeldAbility h)
                h.onStop(world, player, activeStones);
        }

        for (Identifier id : new HashSet<>(GauntletAttributeState.getActive(player))) {
            GauntletAbility ability = GauntletAbilityRegistry.get(id);
            if (ability instanceof AttributeModifierAbility a)
                a.onRemove(player, id);
        }

        GauntletToggleState.clear(player);
        GauntletHeldState.clear(player);
        GauntletAttributeState.clear(player);
    }
}

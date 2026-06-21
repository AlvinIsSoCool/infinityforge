package net.alvin.infinityforge.server.tick;

import net.alvin.infinityforge.infinity.abilities.base.*;
import net.alvin.infinityforge.network.s2c.*;
import net.alvin.infinityforge.registry.GauntletAbilityRegistry;
import net.alvin.infinityforge.server.event.GauntletConnectionEvents;
import net.alvin.infinityforge.server.state.*;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class GauntletServerTick {
    public static void register() { ServerTickEvents.END_SERVER_TICK.register(GauntletServerTick::onTick); }

    private static void onTick(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        int count = players.size();
        if (count == 0) return;

        long time = server.getOverworld().getTime();
        for (int idx = 0; idx < count; idx++) {
            ServerPlayerEntity player = players.get(idx);
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);

            if (stack == null) {
                if (GauntletLastKnownState.wasEquipped(player)) {
                    ItemStack lastStack = GauntletLastKnownState.getLastKnownStack(player);
                    if (lastStack != null) GauntletConnectionEvents.cleanupPlayer(player, lastStack);
                }
                GauntletLastKnownState.setEquipped(player, false, null);
                continue;
            }

            ServerWorld world = (ServerWorld) player.getWorld();
            UUID gauntletId = InfinityGauntletItem.getOrCreateGauntletId(stack);
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            List<InfinityStoneType> lastStones = GauntletLastKnownState.getLastKnownStones(player);
            GauntletLastKnownState.setLastKnownStones(player, activeStones);

            List<ActiveAbility> actives = InfinityGauntletItem.getActiveAbilities(activeStones);
            List<PassiveAbility> passives = InfinityGauntletItem.getPassiveAbilities(activeStones);
            List<ToggleAbility> toggles = InfinityGauntletItem.getToggleAbilities(activeStones);
            List<HeldAbility> helds = InfinityGauntletItem.getHeldAbilities(activeStones);

            if (!lastStones.equals(activeStones)) {
                List<InfinityStoneType> removedStones = new ArrayList<>(lastStones);
                removedStones.removeAll(activeStones);

                for (Identifier id : new HashSet<>(GauntletToggleState.getActive(player))) {
                    GauntletAbility ability = GauntletAbilityRegistry.get(id);
                    if (ability instanceof ToggleAbility t) {
                        boolean ownerRemoved = removedStones.stream()
                                .anyMatch(stone -> stone.gauntletAbilities().contains(t));
                        if (ownerRemoved || !t.meetsCondition(activeStones)) {
                            GauntletToggleState.setActive(player, t.getId(), false);
                            t.onDisable(world, player, activeStones);
                            ServerPlayNetworking.send(player, new SyncToggleStateS2CPacket(t.getId(), false));
                        }
                    }
                }

                for (Identifier id : new HashSet<>(GauntletHeldState.getHeld(player))) {
                    GauntletAbility ability = GauntletAbilityRegistry.get(id);
                    if (ability instanceof HeldAbility h) {
                        boolean ownerRemoved = removedStones.stream()
                                .anyMatch(stone -> stone.gauntletAbilities().contains(h));
                        if (ownerRemoved || !h.meetsCondition(activeStones)) {
                            GauntletHeldState.setHeld(player, h.getId(), false);
                            h.onStop(world, player, activeStones);
                            ServerPlayNetworking.send(player, new SyncHeldForceStopS2CPacket(h.getId()));
                        }
                    }
                }

                List<PassiveAbility> lastPassives = InfinityGauntletItem.getPassiveAbilities(lastStones);
                for (PassiveAbility p : lastPassives) {
                    if (p instanceof LifecyclePassiveAbility lp) {
                        boolean ownerRemoved = removedStones.stream()
                                .anyMatch(stone -> stone.gauntletAbilities().contains(lp));
                        if (ownerRemoved || !lp.meetsCondition(activeStones))
                            lp.triggerEnd(world, player, activeStones);
                    }
                }

                GauntletLastKnownState.setLastKnownStones(player, activeStones);
            }

            ItemStack lastStack = GauntletLastKnownState.getLastKnownStack(player);
            boolean equippedLastTick = GauntletLastKnownState.wasEquipped(player);
            boolean gauntletChanged = stack != lastStack;
            GauntletLastKnownState.setEquipped(player, true, stack);

            if (equippedLastTick && gauntletChanged) {
                if (lastStack != null) GauntletConnectionEvents.cleanupPlayer(player, lastStack);
                ServerPlayNetworking.send(player, new ClearGauntletClientStateS2CPacket());
            }

            if (!equippedLastTick || gauntletChanged) {
                InfinityGauntletItem.loadFromStack(stack, gauntletId);

                for (int i = 0; i < toggles.size(); i++) {
                    ToggleAbility t = toggles.get(i);
                    int charge = GauntletChargeState.getCharge(gauntletId, t.getId(), t.getMaxChargeTicks());
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(t.getId(), charge, t.getMaxChargeTicks()));
                    ServerPlayNetworking.send(player,
                            new SyncToggleStateS2CPacket(t.getId(), GauntletToggleState.isActive(player, t.getId())));
                }

                for (int i = 0; i < helds.size(); i++) {
                    HeldAbility h = helds.get(i);
                    int charge = GauntletChargeState.getCharge(gauntletId, h.getId(), h.getMaxChargeTicks());
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(h.getId(), charge, h.getMaxChargeTicks()));
                }

                for (int i = 0; i < actives.size(); i++) {
                    ActiveAbility a = actives.get(i);
                    long expiry = GauntletCooldownState.getExpiryTick(gauntletId, a.getId());
                    int remaining = (int)(expiry - time);
                    if (remaining > 0) {
                        ServerPlayNetworking.send(player, new SyncCooldownS2CPacket(
                                a.getId(), a.getCooldownTicks(), expiry - a.getCooldownTicks()));
                    }
                }
            }

            for (int i = 0; i < passives.size(); i++) {
                passives.get(i).onTick(world, player, activeStones);
            }

            for (int i = 0; i < toggles.size(); i++) {
                ToggleAbility t = toggles.get(i);
                boolean active = GauntletToggleState.isActive(player, t.getId());
                int oldCharge = GauntletChargeState.getCharge(gauntletId, t.getId(), t.getMaxChargeTicks());
                int newCharge = oldCharge;

                if (active) {
                    t.onTick(world, player, activeStones);

                    if (t.getMaxChargeTicks() != -1) {
                        newCharge = Math.max(0, oldCharge - 1);

                        if (newCharge == 0) {
                            GauntletToggleState.setActive(player, t.getId(), false);
                            t.onDisable(world, player, activeStones);
                            ServerPlayNetworking.send(player,
                                    new SyncToggleStateS2CPacket(t.getId(), false));
                            ServerPlayNetworking.send(player,
                                    new SyncChargeS2CPacket(t.getId(), 0, t.getMaxChargeTicks()));
                        }
                    }
                } else {
                    if (t.getMaxChargeTicks() != -1 && oldCharge < t.getMaxChargeTicks()) {
                        int rate = t.getRefillRateTicks();
                        if (rate > 0 && (time + idx) % rate == 0) {
                            newCharge = oldCharge + 1;
                        } else if (rate < 0) {
                            newCharge = Math.min(t.getMaxChargeTicks(), oldCharge + (-rate));
                        }
                    }
                }

                if (newCharge != oldCharge) {
                    GauntletChargeState.setCharge(gauntletId, t.getId(), newCharge);
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(t.getId(), newCharge, t.getMaxChargeTicks()));
                }
            }

            for (int i = 0; i < helds.size(); i++) {
                HeldAbility h = helds.get(i);
                boolean active = GauntletHeldState.isHeld(player, h.getId());
                int oldCharge  = GauntletChargeState.getCharge(gauntletId, h.getId(), h.getMaxChargeTicks());
                int newCharge  = oldCharge;

                if (active) {
                    h.onTick(world, player, activeStones);

                    if (h.getMaxChargeTicks() != -1) {
                        newCharge = Math.max(0, oldCharge - 1);

                        if (newCharge == 0) {
                            GauntletHeldState.setHeld(player, h.getId(), false);
                            h.onStop(world, player, activeStones);
                            ServerPlayNetworking.send(player,
                                    new SyncHeldForceStopS2CPacket(h.getId()));
                            ServerPlayNetworking.send(player,
                                    new SyncChargeS2CPacket(h.getId(), 0, h.getMaxChargeTicks()));
                        }
                    }
                } else {
                    if (h.getMaxChargeTicks() != -1 && oldCharge < h.getMaxChargeTicks()) {
                        int rate = h.getRefillRateTicks();
                        if (rate > 0 && (time + idx) % rate == 0) {
                            newCharge = oldCharge + 1;
                        } else if (rate < 0) {
                            newCharge = Math.min(h.getMaxChargeTicks(), oldCharge + (-rate));
                        }
                    }
                }

                if (newCharge != oldCharge) {
                    GauntletChargeState.setCharge(gauntletId, h.getId(), newCharge);
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(h.getId(), newCharge, h.getMaxChargeTicks()));
                }
            }
        }
    }
}

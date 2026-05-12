package net.alvin.infinityforge.server.tick;

import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.server.state.GauntletChargeState;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.alvin.infinityforge.server.state.GauntletToggleState;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.network.s2c.SyncChargeS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncHeldForceStopS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncToggleStateS2CPacket;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;

import static net.alvin.infinityforge.server.event.GauntletConnectionEvents.cleanupPlayer;

public class GauntletServerTick {
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(GauntletServerTick::onTick);
    }

    private static void onTick(MinecraftServer server) {
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        int count = players.size();
        if (count == 0) return;

        long time = server.getOverworld().getTime();
        for (int idx = 0; idx < count; idx++) {
            ServerPlayerEntity player = players.get(idx);

            ItemStack stack = InfinityGauntletItem.findGauntlet(player);

            if (stack == null) {
                if (GauntletChargeState.wasEquipped(player)) {
                    cleanupPlayer(player);
                }
                GauntletChargeState.setEquipped(player, false);
                continue;
            }

            ServerWorld world = (ServerWorld) player.getWorld();

            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(stack);
            List<PassiveAbility> passiveAbilities = InfinityGauntletItem.getPassiveAbilities(activeStones);
            List<ToggleAbility> toggleAbilities = InfinityGauntletItem.getToggleAbilities(activeStones);
            List<HeldAbility> heldAbilities = InfinityGauntletItem.getHeldAbilities(activeStones);

            boolean equippedLastTick = GauntletChargeState.wasEquipped(player);
            GauntletChargeState.setEquipped(player, true);

            if (!equippedLastTick) {
                for (int i = 0; i < toggleAbilities.size(); i++) {
                    ToggleAbility t = toggleAbilities.get(i);
                    int charge = GauntletChargeState.getCharge(player, t.getId(), t.getMaxChargeTicks());
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(t.getId(), charge, t.getMaxChargeTicks()));
                    ServerPlayNetworking.send(player,
                            new SyncToggleStateS2CPacket(t.getId(), GauntletToggleState.isActive(player, t.getId())));
                }
                for (int i = 0; i < heldAbilities.size(); i++) {
                    HeldAbility h = heldAbilities.get(i);
                    int charge = GauntletChargeState.getCharge(player, h.getId(), h.getMaxChargeTicks());
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(h.getId(), charge, h.getMaxChargeTicks()));
                }
            }

            // Passive abilities
            for (int i = 0; i < passiveAbilities.size(); i++) {
                passiveAbilities.get(i).onTick(world, player, activeStones);
            }

            // Toggle abilities
            for (int i = 0; i < toggleAbilities.size(); i++) {
                ToggleAbility t = toggleAbilities.get(i);
                boolean active = GauntletToggleState.isActive(player, t.getId());
                int oldCharge = GauntletChargeState.getCharge(player, t.getId(), t.getMaxChargeTicks());
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
                    // Staggered by player index. No tick spike potential. Untested on multiplayer!
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
                    GauntletChargeState.setCharge(player, t.getId(), newCharge);
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(t.getId(), newCharge, t.getMaxChargeTicks()));
                }
            }

            // Held abilities
            for (int i = 0; i < heldAbilities.size(); i++) {
                HeldAbility h = heldAbilities.get(i);
                boolean active = GauntletHeldState.isHeld(player, h.getId());
                int oldCharge = GauntletChargeState.getCharge(player, h.getId(), h.getMaxChargeTicks());
                int newCharge = oldCharge;

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
                    GauntletChargeState.setCharge(player, h.getId(), newCharge);
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(h.getId(), newCharge, h.getMaxChargeTicks()));
                }
            }
        }
    }
}

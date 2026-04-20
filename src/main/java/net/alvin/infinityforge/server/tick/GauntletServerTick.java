package net.alvin.infinityforge.server.tick;

import net.alvin.infinityforge.abilities.HeldAbility;
import net.alvin.infinityforge.abilities.ToggleAbility;
import net.alvin.infinityforge.server.state.GauntletChargeState;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.alvin.infinityforge.server.state.GauntletToggleState;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
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
    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(GauntletServerTick::onTick);
    }

    private static void onTick(MinecraftServer server) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ItemStack stack = InfinityGauntletItem.findGauntlet(player);

            if (stack == null) {
                GauntletChargeState.setEquipped(player, false);
                cleanupPlayer(player);
                continue;
            }

            InfinityGauntletItem gauntlet = (InfinityGauntletItem) stack.getItem();
            List<InfinityStoneType> activeStones = gauntlet.getAddedStones(stack);
            ServerWorld world = (ServerWorld) player.getWorld();

            // Sync all charges on re-equip
            boolean equippedLastTick = GauntletChargeState.wasEquipped(player);
            GauntletChargeState.setEquipped(player, true);

            if (!equippedLastTick) {
                for (ToggleAbility t : gauntlet.getToggleAbilities(stack)) {
                    int charge = GauntletChargeState.getCharge(player, t.getId(), t.getMaxChargeTicks());
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(t.getId(), charge, t.getMaxChargeTicks()));
                    ServerPlayNetworking.send(player,
                            new SyncToggleStateS2CPacket(t.getId(), GauntletToggleState.isActive(player, t.getId())));
                }
                for (HeldAbility h : gauntlet.getHeldAbilities(stack)) {
                    int charge = GauntletChargeState.getCharge(player, h.getId(), h.getMaxChargeTicks());
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(h.getId(), charge, h.getMaxChargeTicks()));
                }
            }

            // Passives
            gauntlet.getPassiveAbilities(stack)
                    .forEach(p -> p.onTick(world, player, activeStones));

            // Toggle abilities
            for (ToggleAbility t : gauntlet.getToggleAbilities(stack)) {
                boolean active = GauntletToggleState.isActive(player, t.getId());
                int oldCharge = GauntletChargeState.getCharge(player, t.getId(), t.getMaxChargeTicks());
                int newCharge = oldCharge;

                if (active) {
                    t.onTick(world, player, activeStones);
                    newCharge = Math.max(0, oldCharge - 1);

                    if (newCharge == 0) {
                        GauntletToggleState.setActive(player, t.getId(), false);
                        t.onDisable(world, player, activeStones);
                        ServerPlayNetworking.send(player,
                                new SyncToggleStateS2CPacket(t.getId(), false));
                    }
                } else {
                    if (oldCharge < t.getMaxChargeTicks()
                            && world.getTime() % t.getRefillRateTicks() == 0) {
                        newCharge = oldCharge + 1;
                    }
                }

                if (newCharge != oldCharge) {
                    GauntletChargeState.setCharge(player, t.getId(), newCharge);
                    ServerPlayNetworking.send(player,
                            new SyncChargeS2CPacket(t.getId(), newCharge, t.getMaxChargeTicks()));
                }
            }

            // Held abilities
            for (HeldAbility h : gauntlet.getHeldAbilities(stack)) {
                boolean active = GauntletHeldState.isHeld(player, h.getId());
                int oldCharge = GauntletChargeState.getCharge(player, h.getId(), h.getMaxChargeTicks());
                int newCharge = oldCharge;

                if (active) {
                    h.onTick(world, player, activeStones);
                    newCharge = Math.max(0, oldCharge - 1);

                    if (newCharge == 0) {
                        GauntletHeldState.setHeld(player, h.getId(), false);
                        h.onStop(world, player, activeStones);
                        ServerPlayNetworking.send(player,
                                new SyncHeldForceStopS2CPacket(h.getId()));
                    }
                } else {
                    if (oldCharge < h.getMaxChargeTicks()
                            && world.getTime() % h.getRefillRateTicks() == 0) {
                        newCharge = oldCharge + 1;
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

package net.alvin.infinityforge.client.packet;

import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.network.s2c.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class GauntletClientPacketHandlers {
    public static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
                SyncToggleStateS2CPacket.TYPE, GauntletClientPacketHandlers::onToggleSync);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncChargeS2CPacket.TYPE, GauntletClientPacketHandlers::onChargeSync);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncHeldForceStopS2CPacket.TYPE, GauntletClientPacketHandlers::onHeldForceStop);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncCooldownS2CPacket.TYPE, GauntletClientPacketHandlers::onCooldownSync);
        ClientPlayNetworking.registerGlobalReceiver(
                ClearGauntletClientStateS2CPacket.TYPE, GauntletClientPacketHandlers::onClearClientState);
    }


    private static void onToggleSync(SyncToggleStateS2CPacket packet,
                                     ClientPlayerEntity player, PacketSender responseSender) {
        if (packet.active()) GauntletClientState.ACTIVE_TOGGLES.add(packet.abilityId());
        else GauntletClientState.ACTIVE_TOGGLES.remove(packet.abilityId());
    }

    private static void onChargeSync(SyncChargeS2CPacket packet,
                                     ClientPlayerEntity player, PacketSender responseSender) {
        int[] existing = GauntletClientState.CHARGES.get(packet.abilityId());
        if (existing != null) {
            existing[0] = packet.charge();
            existing[1] = packet.maxCharge();
        } else {
            GauntletClientState.CHARGES.put(packet.abilityId(), new int[]{ packet.charge(), packet.maxCharge() });
        }
    }

    private static void onHeldForceStop(SyncHeldForceStopS2CPacket packet,
                                        ClientPlayerEntity player, PacketSender responseSender) {
        GauntletClientState.HELD_ACTIVE.remove(packet.abilityId());
        GauntletClientState.HELD_LOCKED_OUT.add(packet.abilityId());
    }

    private static void onCooldownSync(SyncCooldownS2CPacket packet,
                                     ClientPlayerEntity player, PacketSender responseSender) {
        long[] existing = GauntletClientState.COOLDOWNS.get(packet.abilityId());
        if (existing != null) {
            existing[0] = packet.startTick();
            existing[1] = packet.durationTicks();
        } else {
            GauntletClientState.COOLDOWNS.put(packet.abilityId(), new long[]{ packet.startTick(), packet.durationTicks() });
        }
    }

    private static void onClearClientState(ClearGauntletClientStateS2CPacket packet,
                                       ClientPlayerEntity player, PacketSender responseSender) {
        GauntletClientState.clearAll();
    }
}

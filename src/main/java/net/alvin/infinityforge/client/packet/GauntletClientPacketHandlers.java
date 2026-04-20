package net.alvin.infinityforge.client.packet;

import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.network.s2c.SyncChargeS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncCooldownS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncHeldForceStopS2CPacket;
import net.alvin.infinityforge.network.s2c.SyncToggleStateS2CPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;

public class GauntletClientPacketHandlers {
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(
                SyncToggleStateS2CPacket.TYPE, GauntletClientPacketHandlers::onToggleSync);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncChargeS2CPacket.TYPE, GauntletClientPacketHandlers::onChargeSync);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncHeldForceStopS2CPacket.TYPE, GauntletClientPacketHandlers::onHeldForceStop);
        ClientPlayNetworking.registerGlobalReceiver(
                SyncCooldownS2CPacket.TYPE, GauntletClientPacketHandlers::onCooldownSync);
        //ClientPlayNetworking.registerGlobalReceiver(
                //ClearChargesS2CPacket.TYPE, GauntletClientPacketHandlers::onClearCharges);
        //ClientPlayNetworking.registerGlobalReceiver(
               // SyncSizeS2CPacket.TYPE, GauntletClientPacketHandlers::onSizeSync);
    }

    private static void onToggleSync(SyncToggleStateS2CPacket packet,
                                     ClientPlayerEntity player, PacketSender responseSender) {
        if (packet.active()) GauntletClientState.activeToggles.add(packet.abilityId());
        else GauntletClientState.activeToggles.remove(packet.abilityId());
    }

    private static void onChargeSync(SyncChargeS2CPacket packet,
                                     ClientPlayerEntity player, PacketSender responseSender) {
        GauntletClientState.charges.put(
                packet.abilityId(),
                new int[]{ packet.charge(), packet.maxCharge() }
        );
    }

    private static void onHeldForceStop(SyncHeldForceStopS2CPacket packet,
                                        ClientPlayerEntity player, PacketSender responseSender) {
        GauntletClientState.heldActive.remove(packet.abilityId());
        GauntletClientState.heldLockedOut.add(packet.abilityId());
    }

    private static void onCooldownSync(SyncCooldownS2CPacket packet,
                                     ClientPlayerEntity player, PacketSender responseSender) {
        GauntletClientState.cooldowns.put(
                packet.abilityId(),
                new long[]{ packet.startTick(), packet.durationTicks() }
        );
    }
}

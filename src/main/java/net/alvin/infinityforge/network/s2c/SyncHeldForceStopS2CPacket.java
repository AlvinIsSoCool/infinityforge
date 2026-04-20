package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

// Force stop held ability when charge runs out
public record SyncHeldForceStopS2CPacket(Identifier abilityId) implements FabricPacket {
    public static final PacketType<SyncHeldForceStopS2CPacket> TYPE =
            PacketType.create(
                    new Identifier(InfinityForge.MOD_ID, "sync_held_force_stop"),
                    SyncHeldForceStopS2CPacket::new
            );

    public SyncHeldForceStopS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier());
    }

    @Override
    public void write(PacketByteBuf buf) { buf.writeIdentifier(abilityId); }

    @Override
    public PacketType<?> getType() { return TYPE; }
}

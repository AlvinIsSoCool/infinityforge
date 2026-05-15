package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

// Tells client which toggles are currently on for HUD rendering
public record SyncToggleStateS2CPacket(Identifier abilityId, boolean active) implements FabricPacket {
    public static final PacketType<SyncToggleStateS2CPacket> TYPE =
            PacketType.create(
                    new Identifier(InfinityForge.MOD_ID, "sync_toggle_state"),
                    SyncToggleStateS2CPacket::new
            );

    public SyncToggleStateS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(abilityId);
        buf.writeBoolean(active);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}
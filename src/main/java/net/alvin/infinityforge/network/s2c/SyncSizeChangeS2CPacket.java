package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record SyncSizeChangeS2CPacket(float scale) implements FabricPacket {
    public static final PacketType<SyncSizeChangeS2CPacket> TYPE =
            PacketType.create(
                    new Identifier(InfinityForge.MOD_ID, "sync_size_change"),
                    SyncSizeChangeS2CPacket::new
            );

    public SyncSizeChangeS2CPacket(PacketByteBuf buf) {
        this(buf.readFloat());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(scale);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}

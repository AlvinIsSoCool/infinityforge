package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;

public record SyncKnownDimensionsS2CPacket(List<Identifier> dimIds) implements FabricPacket {
    public static final PacketType<SyncKnownDimensionsS2CPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "sync_known_dimensions"),
                    SyncKnownDimensionsS2CPacket::new);

    public SyncKnownDimensionsS2CPacket(PacketByteBuf buf) {
        this(buf.readList(PacketByteBuf::readIdentifier));
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeCollection(dimIds, PacketByteBuf::writeIdentifier);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}
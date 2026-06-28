package net.alvin.infinityforge.network.c2s;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record OpenPortalC2SPacket(int x, int y, int z, Identifier dimId) implements FabricPacket {
    public static final PacketType<OpenPortalC2SPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "open_portal"),
                    OpenPortalC2SPacket::new);

    public OpenPortalC2SPacket(PacketByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readIdentifier());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeIdentifier(dimId);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}
package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record OpenPortalScreenS2CPacket() implements FabricPacket {
    public static final PacketType<OpenPortalScreenS2CPacket> TYPE =
            PacketType.create(
                    new Identifier(InfinityForge.MOD_ID, "open_portal_screen"),
                    OpenPortalScreenS2CPacket::new);

    public OpenPortalScreenS2CPacket(PacketByteBuf buf) {
        this();
    }

    @Override
    public void write(PacketByteBuf buf) {}

    @Override
    public PacketType<?> getType() { return TYPE; }
}

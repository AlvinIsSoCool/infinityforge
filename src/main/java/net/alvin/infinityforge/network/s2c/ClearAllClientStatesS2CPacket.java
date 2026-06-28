package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record ClearAllClientStatesS2CPacket() implements FabricPacket {
    public static final PacketType<ClearAllClientStatesS2CPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "clear_all_client_states"),
                    ClearAllClientStatesS2CPacket::new);

    public ClearAllClientStatesS2CPacket(PacketByteBuf buf) {
        this();
    }

    @Override
    public void write(PacketByteBuf buf) {}

    @Override
    public PacketType<?> getType() { return TYPE; }
}

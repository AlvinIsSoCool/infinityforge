package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record ClearGauntletClientStateS2CPacket() implements FabricPacket {
    public static final PacketType<ClearGauntletClientStateS2CPacket> TYPE =
            PacketType.create(
                    new Identifier(InfinityForge.MOD_ID, "clear_gauntlet_client_state"),
                    ClearGauntletClientStateS2CPacket::new
            );

    public ClearGauntletClientStateS2CPacket(PacketByteBuf buf) {
        this();
    }

    @Override
    public void write(PacketByteBuf buf) {}

    @Override
    public PacketType<?> getType() { return TYPE; }
}

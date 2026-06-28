package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record SyncChargeS2CPacket(Identifier abilityId, int charge, int maxCharge) implements FabricPacket {
    public static final PacketType<SyncChargeS2CPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "sync_charge"),
                    SyncChargeS2CPacket::new);

    public SyncChargeS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readInt(), buf.readInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(abilityId);
        buf.writeInt(charge);
        buf.writeInt(maxCharge);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}
package net.alvin.infinityforge.network.c2s;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record PickupInfinityItemC2SPacket(int entityId) implements FabricPacket {
    public static final PacketType<PickupInfinityItemC2SPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "pickup_stone"),
                    PickupInfinityItemC2SPacket::new);

    public PickupInfinityItemC2SPacket(PacketByteBuf buf) { this(buf.readInt()); }

    @Override
    public void write(PacketByteBuf buf) { buf.writeInt(entityId); }

    @Override
    public PacketType<?> getType() { return TYPE; }
}
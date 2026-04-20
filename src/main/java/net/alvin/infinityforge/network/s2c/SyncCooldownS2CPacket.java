package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record SyncCooldownS2CPacket(Identifier abilityId, int durationTicks, long startTick) implements FabricPacket {
    public static final PacketType<SyncCooldownS2CPacket> TYPE =
            PacketType.create(
                    new Identifier(InfinityForge.MOD_ID, "sync_cooldown"),
                    SyncCooldownS2CPacket::new
            );

    public SyncCooldownS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readInt(), buf.readLong());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(abilityId);
        buf.writeInt(durationTicks);
        buf.writeLong(startTick);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}
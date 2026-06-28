package net.alvin.infinityforge.network.c2s;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record GauntletHeldC2SPacket(Identifier abilityId, boolean pressing) implements FabricPacket {
    public static final PacketType<GauntletHeldC2SPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "gauntlet_held"),
                    GauntletHeldC2SPacket::new);

    public GauntletHeldC2SPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(abilityId);
        buf.writeBoolean(pressing);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}

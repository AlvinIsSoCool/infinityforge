package net.alvin.infinityforge.network.c2s;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record ItemSelectionC2SPacket(Identifier id, boolean shiftClicked) implements FabricPacket {
    public static final PacketType<ItemSelectionC2SPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "item_selection"),
                    ItemSelectionC2SPacket::new);

    public ItemSelectionC2SPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readBoolean());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(id);
        buf.writeBoolean(shiftClicked);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}

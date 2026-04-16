package net.alvin.infinityforge.packets;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record GauntletAbilityC2SPacket(Identifier abilityId) implements FabricPacket {
    public static final PacketType<GauntletAbilityC2SPacket> TYPE = PacketType.create(
            new Identifier(InfinityForge.MOD_ID, "gauntlet_ability"),
            GauntletAbilityC2SPacket::new
    );

    public GauntletAbilityC2SPacket(PacketByteBuf buf) {
        this(buf.readIdentifier());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(abilityId);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}

package net.alvin.infinityforge.network.s2c;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public record SyncAbilityDynamicIconS2CPacket(Identifier abilityId, ItemStack iconStack) implements FabricPacket {
    public static final PacketType<SyncAbilityDynamicIconS2CPacket> TYPE =
            PacketType.create(new Identifier(InfinityForge.MOD_ID, "sync_ability_dynamic_icon"),
                    SyncAbilityDynamicIconS2CPacket::new);

    public SyncAbilityDynamicIconS2CPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readItemStack());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(abilityId);
        buf.writeItemStack(iconStack);
    }

    @Override
    public PacketType<?> getType() { return TYPE; }
}

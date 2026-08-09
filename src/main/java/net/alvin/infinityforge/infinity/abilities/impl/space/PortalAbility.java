package net.alvin.infinityforge.infinity.abilities.impl.space;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.network.s2c.OpenPortalScreenS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class PortalAbility extends ActiveAbility {
    public PortalAbility(Identifier id, AbilityIcon icon,
                         Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                         int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        ServerPlayNetworking.send(player, new OpenPortalScreenS2CPacket());
        return true;
    }
}

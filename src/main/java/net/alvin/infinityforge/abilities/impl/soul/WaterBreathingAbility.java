package net.alvin.infinityforge.abilities.impl.soul;

import net.alvin.infinityforge.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class WaterBreathingAbility extends PassiveAbility {
    public WaterBreathingAbility(Identifier id, Identifier icon,
                             String key, int color) {
        super(id, icon, key, color);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.getAir() < player.getMaxAir()) {
            player.setAir(player.getMaxAir());
        }
    }
}

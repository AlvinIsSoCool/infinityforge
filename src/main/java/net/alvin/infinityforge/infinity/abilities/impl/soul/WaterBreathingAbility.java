package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class WaterBreathingAbility extends PassiveAbility {
    public WaterBreathingAbility(Identifier id, AbilityIcon icon,
                             String key, int color) {
        super(id, icon, key, color);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.getAir() < player.getMaxAir()) player.setAir(player.getMaxAir());
    }
}

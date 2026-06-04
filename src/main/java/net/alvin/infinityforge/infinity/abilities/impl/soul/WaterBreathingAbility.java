package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class WaterBreathingAbility extends PassiveAbility {
    public WaterBreathingAbility(Identifier id, AbilityIcon icon,
                                 String key, Supplier<Integer> color) {
        super(id, icon, key, color, List::of);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.getAir() < player.getMaxAir()) player.setAir(player.getMaxAir());
    }
}

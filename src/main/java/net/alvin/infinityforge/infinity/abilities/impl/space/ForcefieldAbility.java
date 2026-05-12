package net.alvin.infinityforge.infinity.abilities.impl.space;

import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class ForcefieldAbility extends ToggleAbility {
    public ForcefieldAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public void onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }
}

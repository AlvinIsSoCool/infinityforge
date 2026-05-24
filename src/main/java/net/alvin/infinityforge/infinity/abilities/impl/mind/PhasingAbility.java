package net.alvin.infinityforge.infinity.abilities.impl.mind;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class PhasingAbility extends ToggleAbility {
    public PhasingAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        player.noClip = true;
        //player.setOnGround(false);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        player.noClip = true;
    }

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        player.noClip = false;
        //player.setOnGround(true);
    }
}

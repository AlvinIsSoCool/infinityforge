package net.alvin.infinityforge.abilities.impl.mind;

import net.alvin.infinityforge.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class FlightAbility extends ToggleAbility {
    public FlightAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public void onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (!player.isSpectator()) {
            player.getAbilities().allowFlying = true;
            player.getAbilities().flying = true;
            player.getAbilities().setFlySpeed(0.25f);
            player.setVelocity(player.getVelocity().x, 1.0, player.getVelocity().z);
            player.velocityModified = true;
            player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (!player.isSpectator()) {
            player.getAbilities().allowFlying = player.isCreative();
            player.getAbilities().flying = false;
            player.getAbilities().setFlySpeed(0.05f);
            player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }
}

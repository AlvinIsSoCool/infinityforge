package net.alvin.infinityforge.infinity.abilities.impl.mind;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class FlightAbility extends ToggleAbility {
    public FlightAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.interactionManager.isSurvivalLike()) {
            player.getAbilities().allowFlying = true;
            player.getAbilities().flying = true;
            player.getAbilities().setFlySpeed(0.25f);
            player.sendAbilitiesUpdate();

            player.setVelocity(player.getVelocity().x, 0.5, player.getVelocity().z);
            player.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(player));
            return true;
        }
        return false;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.interactionManager.isSurvivalLike()) {
            player.getAbilities().allowFlying = false;
            player.getAbilities().flying = false;
            player.getAbilities().setFlySpeed(0.05f);
            player.sendAbilitiesUpdate();
        }
    }
}

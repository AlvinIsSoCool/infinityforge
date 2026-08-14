package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.config.client.InfinityForgeClientConfig;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.particle.ModParticleHelper;
import net.alvin.infinityforge.util.accessor.PlayerEffectsAccess;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class InvisibilityAbility extends ToggleAbility {
    public InvisibilityAbility(Identifier id, AbilityIcon icon,
                               Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                               int maxChargeTicks, int refillRateTicks) {
        super(id, icon, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.infinityforge$setInvisible(true);
        ModParticleHelper.spawnParticlesPlayer(world, player,
                InfinityForgeClientConfig.get().stoneGlintColors.realityStone, 90);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.infinityforge$setInvisible(false);
        ModParticleHelper.spawnParticlesPlayer(world, player,
                InfinityForgeClientConfig.get().stoneGlintColors.realityStone, 90);
    }
}

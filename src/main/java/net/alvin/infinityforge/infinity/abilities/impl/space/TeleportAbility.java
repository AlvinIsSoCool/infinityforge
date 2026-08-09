package net.alvin.infinityforge.infinity.abilities.impl.space;

import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.particle.ModParticleHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Supplier;

public class TeleportAbility extends ActiveAbility {
    public TeleportAbility(Identifier id, AbilityIcon icon,
                           Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                           int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        BlockHitResult hit = (BlockHitResult) player.raycast(
                activeStones.contains(ModStones.POWER) ? 250.0 : 25.0,
                1f, false);

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos().offset(hit.getSide());
            player.getWorld().playSound(
                    null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS, 1.0f, 1.25f
            );
            ModParticleHelper.spawnParticlesPlayer(world, player,
                    InfinityForgeConfig.get().colorOptions.stoneGlintColors.spaceStone, 90);
            player.requestTeleport(pos.getX(), pos.getY() + 0.5, pos.getZ());
            player.getWorld().playSound(
                    null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS, 1.0f, 1.25f
            );
            ModParticleHelper.spawnParticlesPlayer(world, player,
                    InfinityForgeConfig.get().colorOptions.stoneGlintColors.spaceStone, 90);
            return true;
        }

        return false;
    }
}

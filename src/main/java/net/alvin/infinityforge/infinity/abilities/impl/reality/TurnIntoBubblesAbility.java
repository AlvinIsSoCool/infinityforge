package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.function.Supplier;

public class TurnIntoBubblesAbility extends ActiveAbility {
    public TurnIntoBubblesAbility(Identifier id, AbilityIcon icon,
                                  Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                                  int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0, 1.0f, false);
        if (hit.getType() != HitResult.Type.BLOCK) return false;
        BlockPos blockPos = hit.getBlockPos();
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());

        double cx = blockPos.getX() + 0.5;
        double cy = blockPos.getY() + 0.5;
        double cz = blockPos.getZ() + 0.5;

        world.spawnParticles(ParticleTypes.BUBBLE_POP, cx, cy, cz, 50, 0.3, 0.4, 0.3, 0.005);
        world.spawnParticles(ParticleTypes.BUBBLE_POP, cx, cy, cz, 50, 0.5, 0.6, 0.5, 0.025);
        return false;
    }
}

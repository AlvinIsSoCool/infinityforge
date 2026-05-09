package net.alvin.infinityforge.abilities.impl.space;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.ModStones;
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
    public TeleportAbility(Identifier id, Identifier icon,
                           String key, int color,
                           Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        BlockHitResult hit;
        if (activeStones.contains(ModStones.POWER))
             hit = (BlockHitResult) player.raycast(100.0, 0f, false);
        else
             hit = (BlockHitResult) player.raycast(10.0, 0f, false);

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hit.getBlockPos().offset(hit.getSide());
            player.getWorld().playSound(
                    null, player.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS, 1.0f, 1.1f
            );
            player.requestTeleport(pos.getX(), pos.getY() + 1.0, pos.getZ());
            player.getWorld().playSound(
                    null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.PLAYERS, 1.0f, 1.1f
            );
            return true;
        }

        return false;
    }
}

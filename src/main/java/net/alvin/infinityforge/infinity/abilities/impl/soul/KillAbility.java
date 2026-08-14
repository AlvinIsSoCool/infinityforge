package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityDynamicIcon;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Supplier;

public class KillAbility extends ActiveAbility implements AbilityDynamicIcon<Void> {
    public KillAbility(Identifier id, AbilityIcon icon,
                       Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                       int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Vec3d eyePos = player.getEyePos();
        Vec3d lookVec = player.getRotationVec(1.0f);
        Vec3d reach = eyePos.add(lookVec.multiply(3.0));

        EntityHitResult hit = ProjectileUtil.getEntityCollision(
                world, player,
                eyePos, reach,
                new Box(eyePos, reach).expand(1.0),
                Entity::isAlive
        );

        if (hit != null) {
            Entity target = hit.getEntity();
            if (target instanceof EnderDragonPart part) {
                part.owner.setHealth(0f);
                part.owner.getPhaseManager().setPhase(PhaseType.DYING);
            } else if (target instanceof LivingEntity living) {
                DamageSource source = living.getDamageSources().generic();
                InfinityStoneEventHandler.applyDamageInfinity(living, source, false);
            }
        }

        return true;
    }

    @Override
    public ItemStack getDynamicIcon(Void state) {
        return ModItems.SOUL_STONE.getDefaultStack();
    }
}

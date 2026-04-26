package net.alvin.infinityforge.abilities.impl.soul;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.boss.dragon.phase.PhaseType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class KillAbility extends ActiveAbility {
    public KillAbility(Identifier id, Identifier icon,
                                  String key, int color,
                                  Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
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
                living.setHealth(0f);
            }
        }
    }
}

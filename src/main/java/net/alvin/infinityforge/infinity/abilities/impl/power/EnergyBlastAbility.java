package net.alvin.infinityforge.infinity.abilities.impl.power;

import net.alvin.infinityforge.entity.projectile.EnergyBlastEntity;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.function.Supplier;

public class EnergyBlastAbility extends ActiveAbility {
    public EnergyBlastAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        EnergyBlastEntity entity = new EnergyBlastEntity(world, player);
        Vec3d look = player.getRotationVec(1.0f);
        entity.setVelocity(look.x, look.y, look.z, 1.5f, 0.0f);
        world.spawnEntity(entity);
        return false;
    }
}

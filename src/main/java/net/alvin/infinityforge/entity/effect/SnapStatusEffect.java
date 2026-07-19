package net.alvin.infinityforge.entity.effect;

import net.alvin.infinityforge.world.data.SnappedEntitiesState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class SnapStatusEffect extends StatusEffect {
    public static final int EFFECT_MIN_DURATION = 300;
    public static final int EFFECT_MAX_DURATION = 1200;

    public SnapStatusEffect(StatusEffectCategory category, int color) { super(category, color); }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onRemoved(entity, attributes, amplifier);
        if (entity.getWorld().isClient) return;
        if (!entity.isAlive()) return;

        if (!(entity instanceof PlayerEntity)) {
            ServerWorld world = (ServerWorld) entity.getWorld();
            SnappedEntitiesState state = SnappedEntitiesState.get(world);
            String dimId = entity.getWorld().getRegistryKey().getValue().toString();
            Identifier typeId = EntityType.getId(entity.getType());
            Vec3d pos = entity.getPos();
            state.addEntry(dimId, typeId, pos);
        }

        entity.setHealth(0.0f);
    }
}

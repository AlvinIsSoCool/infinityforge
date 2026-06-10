package net.alvin.infinityforge.infinity.abilities.impl.space;

import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.abilities.ModGauntletAbilities;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.server.state.GauntletToggleState;
import net.alvin.infinityforge.server.state.PlayerForcefieldState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class ForcefieldAbility extends ToggleAbility {
    public ForcefieldAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.setForcefieldActive(true);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.setForcefieldActive(false);
    }

    public static boolean onDamageEntity(LivingEntity entity, DamageSource source, float amount) {
        if (entity instanceof ServerPlayerEntity player
                && GauntletToggleState.isActive(player, ModGauntletAbilities.FORCEFIELD.getId())) {
            if (!PlayerForcefieldState.isHit(player))
                PlayerForcefieldState.markHit(player);

            ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(player);
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);
            boolean allStonesEquipped = new HashSet<>(activeStones).containsAll(ModStones.ALL_STONES);

            if (InfinityForgeConfig.get().godMode && allStonesEquipped) return false;
            return source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY);
        }
        return true;
    }
}

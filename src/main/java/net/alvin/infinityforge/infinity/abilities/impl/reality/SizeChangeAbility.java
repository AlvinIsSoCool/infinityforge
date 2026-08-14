package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.util.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.network.s2c.SyncSizeChangeS2CPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class SizeChangeAbility extends ToggleAbility {
    private static final Set<PlayerEntity> SIZE_ACTIVE = Collections.newSetFromMap(new IdentityHashMap<>());
    private final float scale;

    public SizeChangeAbility(Identifier id, AbilityIcon icon,
                             Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                             int maxChargeTicks, int refillRateTicks, float scale) {
        super(id, icon, color, requiredStones, maxChargeTicks, refillRateTicks);
        this.scale = scale;
    }

    @Override
    public boolean onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (SIZE_ACTIVE.contains(player)) return false;
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        float newScale = activeStones.contains(ModStones.POWER)
                ? (this.scale > 1.0f ? this.scale * 2 : (this.scale < 1.0f ? this.scale * 0.5f : this.scale))
                : this.scale;
        access.infinityforge$setScale(newScale);
        player.calculateDimensions();
        ServerPlayNetworking.send(player, new SyncSizeChangeS2CPacket(newScale));
        SIZE_ACTIVE.add(player);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.infinityforge$setScale(1.0f);
        player.calculateDimensions();
        ServerPlayNetworking.send(player, new SyncSizeChangeS2CPacket(1.0f));
        SIZE_ACTIVE.remove(player);
    }
}

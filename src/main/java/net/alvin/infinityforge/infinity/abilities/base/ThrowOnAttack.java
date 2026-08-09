package net.alvin.infinityforge.infinity.abilities.base;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public interface ThrowOnAttack {
    void onThrow(ServerWorld world, ServerPlayerEntity player);
}

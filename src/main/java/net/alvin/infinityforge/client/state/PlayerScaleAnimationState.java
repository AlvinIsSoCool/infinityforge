package net.alvin.infinityforge.client.state;

import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerScaleAnimationState {
    private static final Map<UUID, PlayerAnimationState> STATES = new HashMap<>();
    private static final float TRANSITION_TIME = 1.25f;

    public static void onClientTick(MinecraftClient client) {
        if (client.world == null) {
            STATES.clear();
            return;
        }
        float delta = Math.min(client.getLastFrameDuration(), 0.1f);
        for (PlayerEntity player : client.world.getPlayers()) {
            PlayerEffectsAccess access = (PlayerEffectsAccess) player;
            float target = access.getCustomScale();
            PlayerAnimationState state = STATES.computeIfAbsent(player.getUuid(), id -> new PlayerAnimationState(1.0f));
            state.advance(target, delta, TRANSITION_TIME);
        }
    }

    public static float getAnimatedScale(PlayerEntity player) {
        PlayerAnimationState state = STATES.get(player.getUuid());
        return state != null ? state.currentScale : ((PlayerEffectsAccess) player).getCustomScale();
    }

    private static class PlayerAnimationState {
        float currentScale;
        float transitionStart;
        float transitionTarget;
        float progress = 1.0f;

        PlayerAnimationState(float initial) {
            this.currentScale = this.transitionStart = this.transitionTarget = initial;
        }

        void advance(float target, float delta, float duration) {
            if (Math.abs(target - transitionTarget) > 0.001f) {
                transitionStart = currentScale;
                transitionTarget = target;
                progress = 0.0f;
            }
            if (progress < 1.0f) {
                progress = Math.min(1.0f, progress + delta / duration);
                currentScale = transitionStart + (transitionTarget - transitionStart) * progress;
            }
        }
    }
}

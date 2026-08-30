package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.network.c2s.PickupInfinityItemC2SPacket;
import net.alvin.infinityforge.registry.ModTags;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(
            method = "doItemUse()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$onItemUse(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient)(Object) this;
        if (client.player == null || client.world == null) return;

        ClientPlayerEntity player = client.player;
        if (player.getActiveHand() != Hand.MAIN_HAND) return;

        HitResult crosshairTarget = client.crosshairTarget;
        if (crosshairTarget == null) return;
        if (crosshairTarget.getType() != HitResult.Type.ENTITY) return;

        EntityHitResult entityHit = (EntityHitResult) crosshairTarget;
        Entity targetEntity = entityHit.getEntity();

        if (!(targetEntity instanceof ItemEntity itemEntity)) return;
        if (!(itemEntity.getStack().isIn(ModTags.Items.INFINITY_ITEMS))) return;
        if (itemEntity.squaredDistanceTo(player) > 16.0) return;
        if (itemEntity.isRemoved()) return;

        ClientPlayNetworking.send(new PickupInfinityItemC2SPacket(itemEntity.getId()));
        ci.cancel();
    }
}

package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityTesseractItem;
import net.alvin.infinityforge.network.c2s.PickupInfinityItemC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(
            method = "doItemUse()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onItemUse(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient)(Object) this;
        if (client.player == null) return;
        ClientPlayerEntity player = client.player;

        if (player.getActiveHand() != Hand.MAIN_HAND) return;

        Box box = player.getBoundingBox()
                .stretch(player.getRotationVec(1.0f).multiply(3.0))
                .expand(1.0);
        List<ItemEntity> items = player.getWorld().getEntitiesByClass(
                ItemEntity.class,
                box,
                e -> (e.getStack().getItem() instanceof InfinityStoneItem
                        || e.getStack().getItem() instanceof InfinityGauntletItem
                        || e.getStack().getItem() instanceof InfinityTesseractItem)
        );
        if (items.isEmpty()) return;

        ItemEntity closest = null;
        double dist = Double.MAX_VALUE;
        for (ItemEntity e : items) {
            double d = e.squaredDistanceTo(player);
            if (d < dist) {
                dist = d;
                closest = e;
            }
        }

        if (closest != null) {
            Vec3d eyePos = player.getEyePos();
            Vec3d lookVec = player.getRotationVec(1.0f);
            Vec3d reach = eyePos.add(lookVec.multiply(3.0));
            Box entityBox = closest.getBoundingBox().expand(0.5);

            if (client.interactionManager != null && entityBox.raycast(eyePos, reach).isPresent()) {
                ClientPlayNetworking.send(new PickupInfinityItemC2SPacket(closest.getId()));
                ci.cancel();
            }
        }
    }
}

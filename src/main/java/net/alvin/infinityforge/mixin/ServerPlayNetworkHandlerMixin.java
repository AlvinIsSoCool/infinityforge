package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.abilities.base.ThrowOnAttack;
import net.alvin.infinityforge.item.InfinityTesseractItem;
import net.alvin.infinityforge.registry.GauntletAbilityRegistry;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(
            method = "onPlayerInteractEntity(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$allowItemEntityAttack(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler self = (ServerPlayNetworkHandler)(Object) this;
        ServerPlayerEntity player = self.player;
        ServerWorld world = player.getServerWorld();
        Entity entity = packet.getEntity(world);

        if (!(entity instanceof ItemEntity itemEntity)) return;
        if (!(itemEntity.getStack().getItem() instanceof InfinityTesseractItem item)) return;

        world.playSound(
                null, itemEntity.getBlockPos(),
                SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS,
                1.0f, 1.25f
        );

        world.spawnEntity(
                new ItemEntity(
                        world,
                        itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                        new ItemStack(item.getStoneItem())
                )
        );

        itemEntity.discard();
        ci.cancel();
    }

    @Inject(method = "onHandSwing", at = @At("HEAD"))
    private void infinityforge$throwOnAttack(HandSwingC2SPacket packet, CallbackInfo ci) {
        for (Identifier id : GauntletHeldState.getHeld(this.player)) {
            if (GauntletAbilityRegistry.get(id) instanceof ThrowOnAttack throwable) {
                throwable.onThrow((ServerWorld) player.getWorld(), player);
            }
        }
    }
}

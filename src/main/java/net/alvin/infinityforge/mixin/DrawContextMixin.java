package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.registry.ModItems;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class DrawContextMixin {
    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V",
            at = @At("HEAD"))
    private void onDrawItem(LivingEntity entity, World world, ItemStack stack,
                            int x, int y, int seed, int z, CallbackInfo ci) {
        if (stack.isOf(ModItems.INFINITY_GAUNTLET)) {
            DiffuseLighting.disableGuiDepthLighting();
        }
    }
}
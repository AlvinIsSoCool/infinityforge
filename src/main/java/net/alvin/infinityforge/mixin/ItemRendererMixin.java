package net.alvin.infinityforge.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alvin.infinityforge.item.InfinityStoneItem;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {
    /*@Inject(
            method = "renderBakedItemModel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;getItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"
            )
    )
    public final void renderBakedItemModel(BakedModel model, ItemStack stack,
                                           int light, int overlay,
                                           MatrixStack matrices, VertexConsumer vertices,
                                           CallbackInfo callbackInfo) {
        if (!(stack.getItem() instanceof InfinityStoneItem stoneItem)) return;
        int glintColor = stoneItem.getStoneType().getGlintColor();

        float r = ((glintColor >> 16) & 0xFF) / 255f;
        float g = ((glintColor >> 8) & 0xFF) / 255f;
        float b = (glintColor & 0xFF) / 255f;

        RenderSystem.setShaderColor(r, g, b, 0.8f);
    }

    @Inject(
            method = "renderBakedItemModel",
            at = @At("TAIL")
    )
    public final void afterRenderBakedItemModel(BakedModel model, ItemStack stack,
                                           int light, int overlay,
                                           MatrixStack matrices, VertexConsumer vertices,
                                           CallbackInfo callbackInfo) {
        if (stack.getItem() instanceof InfinityStoneItem) {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }
    }*/

    @Inject(
            method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;getItemGlintConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"
            )
    )
    public final void beforeGlintConsumer(
            ItemStack stack, ModelTransformationMode renderMode,
            boolean leftHanded, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light,
            int overlay, BakedModel model,
            CallbackInfo callbackInfo
    ) {
        if (!(stack.getItem() instanceof InfinityStoneItem stoneItem)) return;
        int glintColor = stoneItem.getStoneType().getGlintColor();

        float r = ((glintColor >> 16) & 0xFF) / 255f;
        float g = ((glintColor >> 8) & 0xFF) / 255f;
        float b = (glintColor & 0xFF) / 255f;

        RenderSystem.setShaderColor(r, g, b, 0.8f);
    }
}

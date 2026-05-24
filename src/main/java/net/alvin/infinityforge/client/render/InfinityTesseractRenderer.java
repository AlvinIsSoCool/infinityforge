package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class InfinityTesseractRenderer {
    private static final Identifier TESSERACT_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/item/stone.png");
    protected static final float SIZE = 0.125f;

    public void render(ItemStack stack, ModelTransformationMode mode,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay,
                       InfinityStoneType stoneType) {
        int glowColor = stoneType.getGlintColor();
        float pulse = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 0.5 + 0.5);
        int glowAlpha = (int)(40 + 100 * pulse);

        matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);

            if (mode == ModelTransformationMode.GUI) {
                matrices.scale(2.1f, 2.1f, 2.1f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f));
            } else if (mode == ModelTransformationMode.FIXED) {
                matrices.scale(2f, 2f, 2f);
                matrices.translate(0f, 0f, -0.05f);
            } else if (mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND
                    || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND) {
                matrices.scale(0.9f, 0.9f, 0.9f);
                matrices.translate(0f, 0.1f, -0.1f);
            }

            Matrix4f pos = matrices.peek().getPositionMatrix();
            Matrix3f norm = matrices.peek().getNormalMatrix();

            ModItemRenderers.STONE_RENDERER.renderInternal(stack, mode, matrices, vertexConsumers, light, overlay, stoneType);

            VertexConsumer outerVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TESSERACT_TEXTURE));
            ModRenderHelper.renderCube(outerVc, pos, norm, SIZE, glowColor, 100, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);

            VertexConsumer glowVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TESSERACT_TEXTURE));
            ModRenderHelper.renderCube(glowVc, pos, norm, SIZE * 1.05f, glowColor, glowAlpha, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);
        matrices.pop();
    }
}

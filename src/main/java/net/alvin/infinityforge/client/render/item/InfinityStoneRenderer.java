package net.alvin.infinityforge.client.render.item;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.render.ModRenderHelper;
import net.alvin.infinityforge.client.render.ModRenderLayers;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class InfinityStoneRenderer {
    private static final Identifier STONE_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/item/stone.png");
    private static final float SIZE = 0.0625f;
    private static final float r = SIZE * 1.05f;

    public void render(ItemStack stack, ModelTransformationMode mode,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay,
                       InfinityStoneType stoneType) {
        matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);
            if (mode == ModelTransformationMode.GUI) {
                matrices.scale(3.5f, 3.5f, 3.5f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f));
            } else if (mode == ModelTransformationMode.FIXED) {
                matrices.scale(3.5f, 3.5f, 3.5f);
            }
            renderInternal(stack, mode, matrices, vertexConsumers, light, overlay, stoneType);
            ModRenderHelper.drawOutlineIfTargeted(matrices, vertexConsumers, mode, stack,
                    new Box(-r, -r, -r, r, r, r));
        matrices.pop();
    }

    public void renderInternal(ItemStack stack, ModelTransformationMode mode,
                            MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                            int light, int overlay, InfinityStoneType stoneType) {
        Matrix4f pos = matrices.peek().getPositionMatrix();
        Matrix3f norm = matrices.peek().getNormalMatrix();

        int baseColor = stoneType.getBaseColor();
        int glintColor = stoneType.getGlintColor();

        VertexConsumer baseVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(
                STONE_TEXTURE));
        ModRenderHelper.renderCube(baseVc, pos, norm, light, overlay, baseColor, 255, SIZE);

        if (FabricLoader.getInstance().isModLoaded("iris")) {
            VertexConsumer glowVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(
                    STONE_TEXTURE));
            float pulse = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 0.5 + 0.5);
            int glowAlpha = (int)(80 + 175 * pulse);
            ModRenderHelper.renderCube(glowVc, pos, norm, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay,
                    glintColor, glowAlpha, r);
        } else if (vertexConsumers instanceof VertexConsumerProvider.Immediate immediate) {
            if (ModRenderLayers.glintColorUniform != null)
                ModRenderLayers.glintColorUniform.set(
                        ((glintColor >> 16) & 0xFF) / 255f,
                        ((glintColor >> 8)  & 0xFF) / 255f,
                        (glintColor         & 0xFF) / 255f,
                        1f
                );
            if (ModRenderLayers.glintTimeUniform != null)
                ModRenderLayers.glintTimeUniform.set(
                        (float)((System.currentTimeMillis() % 100000L) / 1000.0));
            if (ModRenderLayers.glintScreenSizeUniform != null) {
                Window window = MinecraftClient.getInstance().getWindow();
                ModRenderLayers.glintScreenSizeUniform.set((float) window.getFramebufferWidth(),
                        (float) window.getFramebufferHeight());
            }

            VertexConsumer glintVc = immediate.getBuffer(ModRenderLayers.INFINITY_GLINT);
            ModRenderHelper.renderCube(glintVc, pos, norm, LightmapTextureManager.MAX_LIGHT_COORDINATE,
                    OverlayTexture.DEFAULT_UV, 0, 0, SIZE);
            immediate.draw(ModRenderLayers.INFINITY_GLINT);
        }
    }
}

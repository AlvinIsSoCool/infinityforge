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
        int glowColor = stoneType.glintColor();

        float pulse = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 0.5 + 0.5);
        int glowAlpha = (int)(40 + 100 * pulse);

        matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);

            if (mode == ModelTransformationMode.GUI) {
                matrices.scale(2.1f, 2.1f, 2.1f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f));
            } else if (mode == ModelTransformationMode.FIXED) {
                matrices.scale(1f, 1f, 1f);
                matrices.translate(0f, 0.75f, 0f);
            }

            Matrix4f pos = matrices.peek().getPositionMatrix();
            Matrix3f norm = matrices.peek().getNormalMatrix();

            ModItemRenderers.STONE_RENDERER.renderInternal(stack, mode, matrices, vertexConsumers, light, overlay, stoneType);

            VertexConsumer outerVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TESSERACT_TEXTURE));
            renderCube(outerVc, pos, norm, SIZE, glowColor, 100, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);

            VertexConsumer glowVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TESSERACT_TEXTURE));
            renderCube(glowVc, pos, norm, SIZE * 1.05f, glowColor, glowAlpha, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);
        matrices.pop();
    }

    private void renderCube(VertexConsumer vc, Matrix4f pos,
                           Matrix3f norm, float size,
                           int rgb, int alpha,
                           int light, int overlay) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b =  rgb       & 0xFF;

        // DOWN
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -size, -size, -size,  0,1,
                size, -size, -size,  1,1,
                size, -size,  size,  1,0,
                -size, -size,  size,  0,0,
                0,-1,0);
        // UP
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -size,  size,  size,  0,0,
                size,  size,  size,  1,0,
                size,  size, -size,  1,1,
                -size,  size, -size,  0,1,
                0,1,0);
        // NORTH
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                size,  size, -size,  0,0,
                -size,  size, -size,  1,0,
                -size, -size, -size,  1,1,
                size, -size, -size,  0,1,
                0,0,-1);
        // SOUTH
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -size,  size,  size,  0,0,
                size,  size,  size,  1,0,
                size, -size,  size,  1,1,
                -size, -size,  size,  0,1,
                0,0,1);
        // WEST
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -size,  size, -size,  0,0,
                -size,  size,  size,  1,0,
                -size, -size,  size,  1,1,
                -size, -size, -size,  0,1,
                -1,0,0);
        // EAST
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                size,  size,  size,  0,0,
                size,  size, -size,  1,0,
                size, -size, -size,  1,1,
                size, -size,  size,  0,1,
                1,0,0);
    }

    private void quad(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                      int light, int overlay,
                      int r, int g, int b, int a,
                      float x0, float y0, float z0, float u0, float v0,
                      float x1, float y1, float z1, float u1, float v1,
                      float x2, float y2, float z2, float u2, float v2,
                      float x3, float y3, float z3, float u3, float v3,
                      float nx, float ny, float nz) {
        vc.vertex(pos, x0,y0,z0).color(r,g,b,a).texture(u0,v0).overlay(overlay).light(light).normal(norm, nx,ny,nz).next();
        vc.vertex(pos, x1,y1,z1).color(r,g,b,a).texture(u1,v1).overlay(overlay).light(light).normal(norm, nx,ny,nz).next();
        vc.vertex(pos, x2,y2,z2).color(r,g,b,a).texture(u2,v2).overlay(overlay).light(light).normal(norm, nx,ny,nz).next();
        vc.vertex(pos, x3,y3,z3).color(r,g,b,a).texture(u3,v3).overlay(overlay).light(light).normal(norm, nx,ny,nz).next();
    }
}

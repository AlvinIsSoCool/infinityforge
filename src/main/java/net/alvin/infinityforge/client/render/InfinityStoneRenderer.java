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

public class InfinityStoneRenderer {
    private static final Identifier STONE_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/item/infinity_stone.png");
    private static final float S = 0.125f;

    public void render(ItemStack stack, ModelTransformationMode mode,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay,
                       InfinityStoneType stoneType) {
        int baseColor = stoneType.baseColor();
        int glowColor = stoneType.glintColor();

        double t = System.currentTimeMillis();
        float f1 = (float)(
                Math.sin(t / 600.0) +
                0.6 * Math.sin(t / 1400.0)
        ) * 0.5f + 0.5f;
        f1 = Math.max(0f, Math.min(1f, f1));

        int glowAlpha = (int)(80 + 175 * f1);

        //float pulse = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 0.5 + 0.5);
        //int glowAlpha = (int)(80 + 175 * pulse);

        matrices.push();
            matrices.translate(0.5f, 0.5f, 0.5f);

            if (mode == ModelTransformationMode.GUI || mode == ModelTransformationMode.FIXED) {
                matrices.scale(1.75f, 1.75f, 1.75f);
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(30f));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(45f));
            }
            else {
                matrices.scale(0.5f, 0.5f, 0.5f);
            }

            Matrix4f pos = matrices.peek().getPositionMatrix();
            Matrix3f norm = matrices.peek().getNormalMatrix();

            VertexConsumer baseVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(STONE_TEXTURE));
            renderCube(baseVc, pos, norm, S, baseColor, 255, light, overlay);

            VertexConsumer glowVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(STONE_TEXTURE));
            renderCube(glowVc, pos, norm, S * 1.05f, glowColor, glowAlpha, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay);
        matrices.pop();
    }

    private void renderCube(VertexConsumer vc, Matrix4f pos,
                            Matrix3f norm, float s,
                            int rgb, int alpha,
                            int light, int overlay) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b =  rgb       & 0xFF;

        // DOWN
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -s, -s, -s,  0,1,
                s, -s, -s,  1,1,
                s, -s,  s,  1,0,
                -s, -s,  s,  0,0,
                0,-1,0);
        // UP
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -s,  s,  s,  0,0,
                s,  s,  s,  1,0,
                s,  s, -s,  1,1,
                -s,  s, -s,  0,1,
                0,1,0);
        // NORTH
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                s,  s, -s,  0,0,
                -s,  s, -s,  1,0,
                -s, -s, -s,  1,1,
                s, -s, -s,  0,1,
                0,0,-1);
        // SOUTH
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -s,  s,  s,  0,0,
                s,  s,  s,  1,0,
                s, -s,  s,  1,1,
                -s, -s,  s,  0,1,
                0,0,1);
        // WEST
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                -s,  s, -s,  0,0,
                -s,  s,  s,  1,0,
                -s, -s,  s,  1,1,
                -s, -s, -s,  0,1,
                -1,0,0);
        // EAST
        quad(vc, pos, norm,
                light, overlay,
                r, g, b, alpha,
                s,  s,  s,  0,0,
                s,  s, -s,  1,0,
                s, -s, -s,  1,1,
                s, -s,  s,  0,1,
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

package net.alvin.infinityforge.client.render;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModRenderHelper {
    public static void renderCube(VertexConsumer vc, Matrix4f pos,
                            Matrix3f norm, float size,
                            int rgb, int a,
                            int light, int overlay) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        // DOWN
        quad(vc, pos, norm, light, overlay, r, g, b, a,
                -size, -size, -size, 0,1,
                 size, -size, -size, 1,1,
                 size, -size,  size, 1,0,
                -size, -size,  size, 0,0,
                0, -1, 0);
        // UP
        quad(vc, pos, norm, light, overlay, r, g, b, a,
                -size,  size,  size, 0,0,
                 size,  size,  size, 1,0,
                 size,  size, -size, 1,1,
                -size,  size, -size, 0,1,
                0, 1, 0);
        // NORTH
        quad(vc, pos, norm, light, overlay, r, g, b, a,
                 size,  size, -size, 0,0,
                -size,  size, -size, 1,0,
                -size, -size, -size, 1,1,
                 size, -size, -size, 0,1,
                0, 0, -1);
        // SOUTH
        quad(vc, pos, norm, light, overlay, r, g, b, a,
                -size,  size,  size, 0,0,
                 size,  size,  size, 1,0,
                 size, -size,  size, 1,1,
                -size, -size,  size, 0,1,
                0, 0, 1);
        // WEST
        quad(vc, pos, norm, light, overlay, r, g, b, a,
                -size,  size, -size, 0,0,
                -size,  size,  size, 1,0,
                -size, -size,  size, 1,1,
                -size, -size, -size, 0,1,
                -1, 0, 0);
        // EAST
        quad(vc, pos, norm, light, overlay, r, g, b, a,
                size,  size,  size, 0,0,
                size,  size, -size, 1,0,
                size, -size, -size, 1,1,
                size, -size,  size, 0,1,
                1, 0, 0);
    }

    public static void renderCubeGlint(VertexConsumer vc, Matrix4f pos, float size) {
        // DOWN
        quadGlint(vc, pos,
                -size, -size, -size,
                 size, -size, -size,
                 size, -size,  size,
                -size, -size,  size);
        // UP
        quadGlint(vc, pos,
                -size, size,  size,
                 size, size,  size,
                 size, size, -size,
                -size, size, -size);
        // NORTH
        quadGlint(vc, pos,
                 size,  size, -size,
                -size,  size, -size,
                -size, -size, -size,
                 size, -size, -size);
        // SOUTH
        quadGlint(vc, pos,
                -size,  size, size,
                 size,  size, size,
                 size, -size, size,
                -size, -size, size);
        // WEST
        quadGlint(vc, pos,
                -size,  size, -size,
                -size,  size,  size,
                -size, -size,  size,
                -size, -size, -size);
        // EAST
        quadGlint(vc, pos,
                size,  size,  size,
                size,  size, -size,
                size, -size, -size,
                size, -size,  size);
    }

    public static void quad(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                      int light, int overlay,
                      int r, int g, int b, int a,
                      float x0, float y0, float z0, float u0, float v0,
                      float x1, float y1, float z1, float u1, float v1,
                      float x2, float y2, float z2, float u2, float v2,
                      float x3, float y3, float z3, float u3, float v3,
                      float nx, float ny, float nz) {
        ModRenderHelper.putVertex(vc, pos, norm, light, overlay, r, g, b, a, x0, y0, z0, u0, v0, nx, ny, nz);
        ModRenderHelper.putVertex(vc, pos, norm, light, overlay, r, g, b, a, x1, y1, z1, u1, v1, nx, ny, nz);
        ModRenderHelper.putVertex(vc, pos, norm, light, overlay, r, g, b, a, x2, y2, z2, u2, v2, nx, ny, nz);
        ModRenderHelper.putVertex(vc, pos, norm, light, overlay, r, g, b, a, x3, y3, z3, u3, v3, nx, ny, nz);
    }

    public static void quadGlint(VertexConsumer vc, Matrix4f pos,
                                 float x0, float y0, float z0,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3) {
        ModRenderHelper.putVertex(vc, pos, x0, y0, z0);
        ModRenderHelper.putVertex(vc, pos, x1, y1, z1);
        ModRenderHelper.putVertex(vc, pos, x2, y2, z2);
        ModRenderHelper.putVertex(vc, pos, x3, y3, z3);
    }

    public static void putVertex(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                 int light, int overlay,
                                 int r, int g, int b, int a,
                                 float x, float y, float z, float u, float v,
                                 float nx, float ny, float nz) {
        vc.vertex(pos, x, y, z)
                .color(r, g, b, a)
                .texture(u, v)
                .overlay(overlay)
                .light(light)
                .normal(norm, nx, ny, nz)
                .next();
    }

    public static void putVertex(VertexConsumer vc, Matrix4f pos,
                                 float x, float y, float z) {
        vc.vertex(pos, x, y, z).next();
    }

    public static void putVertex(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                           float x, float y, float z, float u, float v) {
        ModRenderHelper.putVertex(vc, pos, norm,
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                255, 255, 255, 255,
                x, y, z, u, v,
                0f, 0f, 1f);
    }

    public static void putVertex(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                 Vector3f pf, float u, float v) {
        ModRenderHelper.putVertex(vc, pos, norm, pf.x, pf.y, pf.z, u, v);
    }
}

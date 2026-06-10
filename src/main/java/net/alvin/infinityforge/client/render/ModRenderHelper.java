package net.alvin.infinityforge.client.render;

import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class ModRenderHelper {

    public static void renderCube(VertexConsumer vc, Matrix4f pos,
                            Matrix3f norm, float size,
                            int rgb, int alpha,
                            int light, int overlay) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        // DOWN
        quad(vc, pos, norm, light, overlay, r, g, b, alpha,
                -size, -size, -size, 0,1,
                size, -size, -size,  1,1,
                size, -size,  size,  1,0,
                -size, -size,  size,  0,0,
                0,-1,0);
        // UP
        quad(vc, pos, norm, light, overlay, r, g, b, alpha,
                -size,  size,  size,  0,0,
                size,  size,  size,  1,0,
                size,  size, -size,  1,1,
                -size,  size, -size,  0,1,
                0,1,0);
        // NORTH
        quad(vc, pos, norm, light, overlay, r, g, b, alpha,
                size,  size, -size,  0,0,
                -size, size, -size,  1,0,
                -size, -size, -size,  1,1,
                size,  -size, -size,  0,1,
                0,0,-1);
        // SOUTH
        quad(vc, pos, norm, light, overlay, r, g, b, alpha,
                -size,  size,  size,  0,0,
                 size,  size,  size,  1,0,
                 size, -size,  size,  1,1,
                -size, -size,  size,  0,1,
                0,0,1);
        // WEST
        quad(vc, pos, norm, light, overlay, r, g, b, alpha,
                -size,  size, -size,  0,0,
                -size,  size,  size,  1,0,
                -size, -size,  size,  1,1,
                -size, -size, -size,  0,1,
                -1,0,0);
        // EAST
        quad(vc, pos, norm, light, overlay, r, g, b, alpha,
                size,  size,  size,  0,0,
                size,  size, -size,  1,0,
                size, -size, -size,  1,1,
                size, -size,  size,  0,1,
                1,0,0);
    }

    public static void renderCubeGlint(VertexConsumer vc, Matrix4f pos, float size) {
        // DOWN
        quadGlint(vc, pos,
                -size,  -size, -size,
                 size,  -size, -size,
                 size,  -size,  size,
                -size,  -size,  size
        );

        // UP
        quadGlint(vc, pos,
                -size, size,  size,
                 size, size,  size,
                 size, size, -size,
                -size, size, -size
        );

        // NORTH
        quadGlint(vc, pos,
                 size,  size, -size,
                -size,  size, -size,
                -size, -size, -size,
                 size, -size, -size
        );
        // SOUTH
        quadGlint(vc, pos,
                -size,  size,  size,
                 size,  size,  size,
                 size, -size,  size,
                -size, -size,  size
        );

        // WEST
        quadGlint(vc, pos,
                -size,  size, -size,
                -size,  size,  size,
                -size, -size,  size,
                -size, -size, -size
        );

        // EAST
        quadGlint(vc, pos,
                size,  size,  size,
                size,  size, -size,
                size, -size, -size,
                size, -size,  size
        );
    }

    public static void quad(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
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

    public static void quadGlint(VertexConsumer vc, Matrix4f pos,
                                 float x0, float y0, float z0,
                                 float x1, float y1, float z1,
                                 float x2, float y2, float z2,
                                 float x3, float y3, float z3) {
        vc.vertex(pos, x0, y0, z0).next();
        vc.vertex(pos, x1, y1, z1).next();
        vc.vertex(pos, x2, y2, z2).next();
        vc.vertex(pos, x3, y3, z3).next();
    }
}

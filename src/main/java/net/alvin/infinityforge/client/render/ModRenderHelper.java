package net.alvin.infinityforge.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModRenderHelper {
    /**
     * Renders a cube from 6 quads.
     * This uses {@link net.minecraft.client.render.VertexFormats#POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL}
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param light The light value
     * @param overlay The overlay value
     * @param rgb The packed color in rgb format
     * @param a The alpha specified separately
     * @param size The size of the cube
     */
    public static void renderCube(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                   int light, int overlay, int rgb, int a, float size) {
        // DOWN
        quad(vc, pos, norm, light, overlay, rgb, a,
                -size, -size, -size, 0, 1,
                 size, -size, -size, 1, 1,
                 size, -size,  size, 1, 0,
                -size, -size,  size, 0, 0,
                0, -1, 0);
        // UP
        quad(vc, pos, norm, light, overlay, rgb, a,
                -size,  size,  size, 0, 0,
                 size,  size,  size, 1, 0,
                 size,  size, -size, 1, 1,
                -size,  size, -size, 0, 1,
                0, 1, 0);
        // NORTH
        quad(vc, pos, norm, light, overlay, rgb, a,
                 size,  size, -size, 0, 0,
                -size,  size, -size, 1, 0,
                -size, -size, -size, 1, 1,
                 size, -size, -size, 0, 1,
                0, 0, -1);
        // SOUTH
        quad(vc, pos, norm, light, overlay, rgb, a,
                -size,  size,  size, 0, 0,
                 size,  size,  size, 1, 0,
                 size, -size,  size, 1, 1,
                -size, -size,  size, 0, 1,
                0, 0, 1);
        // WEST
        quad(vc, pos, norm, light, overlay, rgb, a,
                -size,  size, -size, 0, 0,
                -size,  size,  size, 1, 0,
                -size, -size,  size, 1, 1,
                -size, -size, -size, 0, 1,
                -1, 0, 0);
        // EAST
        quad(vc, pos, norm, light, overlay, rgb, a,
                size,  size,  size, 0, 0,
                size,  size, -size, 1, 0,
                size, -size, -size, 1, 1,
                size, -size,  size, 0, 1,
                1, 0, 0);
    }

    /**
     * Renders a beam using 6 quads.
     * This uses {@link net.minecraft.client.render.VertexFormats#POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL}
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param light The light value
     * @param overlay The overlay value
     * @param rgb The packed color in rgb format
     * @param a The alpha specified separately
     * @param hw The half-width of the beam
     * @param hh The half-height of the beam
     * @param hl The half-length of the beam (in local Z)
     */
    public static void renderBeam(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                  int light, int overlay, int rgb, int a,
                                  float hw, float hh, float hl) {
        // +X
        quad(vc, pos, norm, light, overlay, rgb, a,
                hw, -hh, -hl, 0, 0,
                hw,  hh, -hl, 1, 0,
                hw,  hh,  hl, 1, 1,
                hw, -hh,  hl, 0, 1,
                1, 0, 0);

        // -X
        quad(vc, pos, norm, light, overlay, rgb, a,
                -hw, -hh, -hl, 0, 0,
                -hw, -hh,  hl, 1, 0,
                -hw,  hh,  hl, 1, 1,
                -hw,  hh, -hl, 0, 1,
                -1, 0, 0);

        // +Y (top)
        quad(vc, pos, norm, light, overlay, rgb, a,
                -hw,  hh, -hl, 0, 0,
                -hw,  hh,  hl, 1, 0,
                 hw,  hh,  hl, 1, 1,
                 hw,  hh, -hl, 0, 1,
                0, 1, 0);

        // -Y (bottom)
        quad(vc, pos, norm, light, overlay, rgb, a,
                -hw, -hh, -hl, 0, 0,
                 hw, -hh, -hl, 1, 0,
                 hw, -hh,  hl, 1, 1,
                -hw, -hh,  hl, 0, 1,
                0, -1, 0);

        // front / -Z (nose)
        quad(vc, pos, norm, light, overlay, rgb, a,
                -hw, -hh, -hl, 0, 0,
                -hw,  hh, -hl, 1, 0,
                 hw,  hh, -hl, 1, 1,
                 hw, -hh, -hl, 0, 1,
                0, 0, -1);

        // back / +Z (tail)
        quad(vc, pos, norm, light, overlay, rgb, a,
                -hw, -hh, hl, 0, 0,
                 hw, -hh, hl, 1, 0,
                 hw,  hh, hl, 1, 1,
                -hw,  hh, hl, 0, 1,
                0, 0, 1);
    }

    /**
     * Renders a flat quad.
     * This uses {@link net.minecraft.client.render.VertexFormats#POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL}
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param light The light value
     * @param overlay The overlay value
     * @param r The r value of a color
     * @param g The g value of a color
     * @param b The b value of a color
     * @param a The alpha value of a color
     * @implNote The params x0,y0,z0 -> x3,y3,z3 specify the x,y,z positions
     *           for the 4 vertices<br>The params u0,v0 -> u3,v3 specify the UV offset for
     *           the 4 vertices<br>nx,ny,nz specify the normals for the vertices
     */
    public static void quad(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                      int light, int overlay, int r, int g, int b, int a,
                      float x0, float y0, float z0, float u0, float v0,
                      float x1, float y1, float z1, float u1, float v1,
                      float x2, float y2, float z2, float u2, float v2,
                      float x3, float y3, float z3, float u3, float v3,
                      float nx, float ny, float nz) {
        putVertex(vc, pos, norm, light, overlay, r, g, b, a, x0, y0, z0, u0, v0, nx, ny, nz);
        putVertex(vc, pos, norm, light, overlay, r, g, b, a, x1, y1, z1, u1, v1, nx, ny, nz);
        putVertex(vc, pos, norm, light, overlay, r, g, b, a, x2, y2, z2, u2, v2, nx, ny, nz);
        putVertex(vc, pos, norm, light, overlay, r, g, b, a, x3, y3, z3, u3, v3, nx, ny, nz);
    }

    /**
     * A delegate quad method that takes a packed rgb color value.
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param light The light value
     * @param overlay The overlay value
     * @param a The alpha value of a color
     * @implNote The params x0,y0,z0 -> x3,y3,z3 specify the x,y,z positions
     *           for the 4 vertices.<br>The params u0,v0 -> u3,v3 specify the UV offset for
     *           the 4 vertices.<br>nx,ny,nz specify the normals for the vertices.
     */
    public static void quad(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                            int light, int overlay, int rgb, int a,
                            float x0, float y0, float z0, float u0, float v0,
                            float x1, float y1, float z1, float u1, float v1,
                            float x2, float y2, float z2, float u2, float v2,
                            float x3, float y3, float z3, float u3, float v3,
                            float nx, float ny, float nz) {
        int r = (rgb >> 16) & 0xFF, g = (rgb >> 8) & 0xFF, b = rgb & 0xFF;
        quad(vc, pos, norm, light, overlay, 
                r, g, b, a, 
                x0, y0, z0, u0, v0, 
                x1, y1, z1, u1, v1, 
                x2, y2, z2, u2, v2, 
                x3, y3, z3, u3, v3, 
                nx, ny, nz
        );
    }

    /**
     * Provides a vertex to the VertexConsumer and calls {@link VertexConsumer#next()}
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param light The light value
     * @param overlay The overlay value
     * @param r The r value of a color
     * @param g The g value of a color
     * @param b The b value of a color
     * @param a The alpha value of a color
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param u The u offset
     * @param v The v offset
     * @param nx The normal x-axis
     * @param ny The normal y-axis
     * @param nz The normal z-axis
     */
    public static void putVertex(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                 int light, int overlay, int r, int g, int b, int a,
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

    /**
     * A delegate putVertex function that omits light, overlay,
     * color and normals for prespecified values:
     * {@link LightmapTextureManager#MAX_LIGHT_COORDINATE}, {@link OverlayTexture#DEFAULT_UV},
     * RGBA{255,255,255,255} and {0f,0f,1f} normal values respectively.
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param u The u offset
     * @param v The v offset
     */
    public static void putVertex(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                           float x, float y, float z, float u, float v) {
        putVertex(vc, pos, norm,
                LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                255, 255, 255, 255,
                x, y, z, u, v,
                0f, 0f, 1f);
    }

    /**
     * A delegate putVertex method that takes a Vector3f for position.
     * @param vc The VertexConsumer buffer
     * @param pos The position matrix
     * @param norm The normal matrix
     * @param pf The packed Vector3f positions
     * @param u The u offset
     * @param v The v offset
     */
    public static void putVertex(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                 Vector3f pf, float u, float v) {
        putVertex(vc, pos, norm, pf.x, pf.y, pf.z, u, v);
    }

    public static void drawOutlineIfTargeted(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                             ModelTransformationMode mode, ItemStack stack, Box box) {
        if (mode != ModelTransformationMode.GROUND) return;
        if (!(MinecraftClient.getInstance().crosshairTarget instanceof EntityHitResult hit)) return;
        if (!(hit.getEntity() instanceof ItemEntity ie) || ie.getStack() != stack) return;

        VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getLines());
        WorldRenderer.drawBox(matrices, vc, box, 0f, 0f, 0f, 0.4f);
    }
}

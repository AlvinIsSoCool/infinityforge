package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class InfinityGauntletRenderer {
    private static final Identifier GAUNTLET_TEXTURE_3D = new Identifier(InfinityForge.MOD_ID, "textures/item/infinity_gauntlet_3d.png");
    private static final Identifier SLOT_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/item/stone.png");
    protected static final ModelIdentifier GAUNTLET_MODEL_2D = new ModelIdentifier(new Identifier(InfinityForge.MOD_ID, "infinity_gauntlet_2d"), "inventory");

    public void render(ItemStack stack, ModelTransformationMode mode,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {
        if (mode == ModelTransformationMode.GUI) {
            BakedModel flatModel = MinecraftClient.getInstance()
                    .getItemRenderer()
                    .getModels()
                    .getModelManager()
                    .getModel(GAUNTLET_MODEL_2D);
            MinecraftClient.getInstance().getItemRenderer().renderBakedItemModel(
                    flatModel,
                    stack,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers.getBuffer(TexturedRenderLayers.getItemEntityTranslucentCull())
            );
            return;
        }

        matrices.push();
            matrices.translate(0.5, 1.9, 0.5);
            matrices.scale(-1f, -1f, -1f);

            // Gauntlet Render.
            // Future Note: This whole render happens alonside the transforms
            // mentioned in: infinity_gauntlet.json
            // Do not remove the transforms in the JSON file.
            matrices.push();
                matrices.scale(0.0625f, 0.0625f, 0.0625f);

                Matrix4f pos = matrices.peek().getPositionMatrix();
                Matrix3f norm = new Matrix3f(); // Normal has to be reset because I am lazy,
                                                // I am working with bad model transforms,
                                                // and LightmapTextureManager.MAX_LIGHT_COORDINATE
                                                // doesn't really care about this much.
                VertexConsumer vc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(GAUNTLET_TEXTURE_3D));

                renderBox(vc,pos,norm, LightmapTextureManager.MAX_LIGHT_COORDINATE,overlay,255,255,255,255,
                        -3f, 20f, -3f,  3f, 24f, 3f,
                        0,0, 6,4,6, 64,16);

                renderBox(vc,pos,norm, LightmapTextureManager.MAX_LIGHT_COORDINATE,overlay,255,255,255,255,
                        -2.5f, 16f, -2.5f,  2.5f, 20f, 2.5f,
                        24,0, 5,4,5, 64,16);
            matrices.pop();

            // Stone slot render.
            // Future Note: Positions, transforms and scale are all very fragile.
            // Caution advised before modifying.
            for (InfinityStoneType stoneType : InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY) {
                float[] slot = getSlotTransform(stoneType);
                matrices.push();
                    matrices.translate(slot[0], slot[1], slot[2]);
                    if (slot[3] != 0f)
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(slot[3]));

                    Matrix4f slotPos = matrices.peek().getPositionMatrix();
                    Matrix3f slotNorm = new Matrix3f();
                    VertexConsumer slotVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(SLOT_TEXTURE));

                    float h = 0.031f;
                    quad(slotVc, slotPos, slotNorm,
                            LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay,
                            0x22, 0x22, 0x22, 255,
                            -h,  h, 0,  0, 0,
                            h,  h, 0,  1, 0,
                            h, -h, 0,  1, 1,
                            -h, -h, 0,  0, 1,
                            0, 0, 1);
                matrices.pop();
            }

            // Stones Render.
            List<InfinityStoneType> stones = InfinityGauntletItem.getAddedStones(stack);
            for (InfinityStoneType stoneType : stones) {
                float[] slot = getSlotTransform(stoneType);
                matrices.push();
                    matrices.translate(slot[0], slot[1], slot[2]);

                    // Rotation (for the time stone).
                    if (slot[3] != 0f)
                        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(slot[3]));

                    matrices.translate(0f, 0f, 0.02f); // Stone depth
                    matrices.scale(0.4f, 0.4f, 0.4f); // Stone Size

                    ModItemRenderers.STONE_RENDERER.renderInternal(
                            stack, mode, matrices, vertexConsumers, light, overlay, stoneType);
                matrices.pop();
            }

        matrices.pop();
    }

    private void renderBox(VertexConsumer vc, Matrix4f pos, Matrix3f norm,
                                  int light, int overlay,
                                  int r, int g, int b, int a,
                                  float x0, float y0, float z0,
                                  float x1, float y1, float z1,
                                  int texU, int texV,
                                  int texW, int texH, int texD,
                                  int sheetW, int sheetH) {
        float W  = texW / (float)sheetW;
        float H  = texH / (float)sheetH;
        float Du = texD / (float)sheetW;
        float Dv = texD / (float)sheetH;
        float u  = texU / (float)sheetW;
        float v  = texV / (float)sheetH;

        // DOWN
        quad(vc,pos,norm,light,overlay,r,g,b,a,
                x0,y0,z0, u+Du,       v,
                x1,y0,z0, u+Du+W,     v,
                x1,y0,z1, u+Du+W,     v+Dv,
                x0,y0,z1, u+Du,       v+Dv,
                0,-1,0);
        // UP
        quad(vc,pos,norm,light,overlay,r,g,b,a,
                x0,y1,z1, u+Du+W,     v,
                x1,y1,z1, u+Du+W+W,   v,
                x1,y1,z0, u+Du+W+W,   v+Dv,
                x0,y1,z0, u+Du+W,     v+Dv,
                0,1,0);
        // NORTH
        quad(vc,pos,norm,light,overlay,r,g,b,a,
                x1,y1,z0, u+Du+W,     v+Dv,
                x0,y1,z0, u+Du,       v+Dv,
                x0,y0,z0, u+Du,       v+Dv+H,
                x1,y0,z0, u+Du+W,     v+Dv+H,
                0,0,-1);
        // SOUTH
        quad(vc,pos,norm,light,overlay,r,g,b,a,
                x0,y1,z1, u+Du+W+Du,  v+Dv,
                x1,y1,z1, u+Du+W+Du+W,v+Dv,
                x1,y0,z1, u+Du+W+Du+W,v+Dv+H,
                x0,y0,z1, u+Du+W+Du,  v+Dv+H,
                0,0,1);
        // WEST
        quad(vc,pos,norm,light,overlay,r,g,b,a,
                x0,y1,z0, u,           v+Dv,
                x0,y1,z1, u+Du,        v+Dv,
                x0,y0,z1, u+Du,        v+Dv+H,
                x0,y0,z0, u,           v+Dv+H,
                -1,0,0);
        // EAST
        quad(vc,pos,norm,light,overlay,r,g,b,a,
                x1,y1,z1, u+Du+W,      v+Dv,
                x1,y1,z0, u+Du+W+Du,   v+Dv,
                x1,y0,z0, u+Du+W+Du,   v+Dv+H,
                x1,y0,z1, u+Du+W,      v+Dv+H,
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
        vc.vertex(pos,x0,y0,z0).color(r,g,b,a).texture(u0,v0).overlay(overlay).light(light).normal(norm,nx,ny,nz).next();
        vc.vertex(pos,x1,y1,z1).color(r,g,b,a).texture(u1,v1).overlay(overlay).light(light).normal(norm,nx,ny,nz).next();
        vc.vertex(pos,x2,y2,z2).color(r,g,b,a).texture(u2,v2).overlay(overlay).light(light).normal(norm,nx,ny,nz).next();
        vc.vertex(pos,x3,y3,z3).color(r,g,b,a).texture(u3,v3).overlay(overlay).light(light).normal(norm,nx,ny,nz).next();
    }

    private float[] getSlotTransform(InfinityStoneType stoneType) {
        // format: [x, y, z, rotationY]
        // Future Note: Pixel-perfect alignments. DO NOT CHANGE CARELESSLY!
        String id = InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stoneType).getPath();
        return switch (id) {
            case "soul"    -> new float[]{ 0.119f,  1.06f,  -0.16f, 0f  };
            case "reality" -> new float[]{ 0.044f,  1.06f,  -0.16f, 0f  };
            case "space"   -> new float[]{ -0.0375f, 1.06f, -0.16f, 0f  };
            case "power"   -> new float[]{ -0.118f,  1.06f, -0.16f, 0f  };
            case "mind"    -> new float[]{ 0.006f,  1.160f, -0.16f, 0f  };
            case "time"    -> new float[]{ -0.16f,  1.092f,  0.062f, 90f };
            default        -> new float[]{ 0f, 0f, 0f, 0f };
        };
    }
}

package net.alvin.infinityforge.client.render.entity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.render.ModRenderHelper;
import net.alvin.infinityforge.entity.projectile.EnergyBlastEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EnergyBlastEntityRenderer extends EntityRenderer<EnergyBlastEntity> {
    private static final Identifier TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/entity/energy_blast.png");
    private static final Vector3f MODEL_FORWARD = new Vector3f(0, 0, -1);

    private static final float HW = 0.0625f;
    private static final float HH = 0.0625f;
    private static final float HL = 0.25f;

    public EnergyBlastEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnergyBlastEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
            matrices.multiply(rotationFromDirection(entity.getDirection()));

            MatrixStack.Entry entry = matrices.peek();
            Matrix4f pos = entry.getPositionMatrix();
            Matrix3f norm = entry.getNormalMatrix();
            int stoneBase = entity.getStoneType().getBaseColor();
            int stoneGlint = entity.getStoneType().getGlintColor();

            VertexConsumer innerVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(
                    TEXTURE));
            ModRenderHelper.renderBeam(innerVc, pos, norm,
                    light, OverlayTexture.DEFAULT_UV, stoneBase, 255, HW, HH, HL);

            VertexConsumer outerVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(
                    TEXTURE));
            ModRenderHelper.renderBeam(outerVc, pos, norm,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                    stoneGlint, 100, HW * 1.5f, HH * 1.5f, HL * 1.5f);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(EnergyBlastEntity entity) {
        return TEXTURE;
    }

    private static Quaternionf rotationFromDirection(Vec3d dir) {
        Vector3f to = new Vector3f((float) dir.x, (float) dir.y, (float) dir.z);
        if (to.lengthSquared() < 1.0e-6f) return new Quaternionf();
        to.normalize();
        return new Quaternionf().rotationTo(MODEL_FORWARD, to);
    }
}

package net.alvin.infinityforge.client.render.entity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.render.ModRenderHelper;
import net.alvin.infinityforge.entity.EnergyBeamEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EnergyBeamEntityRenderer extends EntityRenderer<EnergyBeamEntity> {
    private static final Identifier TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/entity/energy_blast.png");
    private static final Vector3f MODEL_FORWARD = new Vector3f(0, 0, -1);

    private static final float HW = 0.0625f;
    private static final float HH = 0.0625f;
    private static final float ROLL_DEGREES_PER_TICK = 6f;

    public EnergyBeamEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EnergyBeamEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
            Vec3d dir = entity.getRotationVec(tickDelta);
            matrices.multiply(rotationFromDirection(dir));

            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d camPos = client.gameRenderer.getCamera().getPos();
            boolean firstPerson = client.options.getPerspective().isFirstPerson();
            double distToCamera = entity.getPos().distanceTo(camPos);
            float startOffset = (firstPerson && distToCamera < 1.0) ? 0.3f : 0f;

            float length = entity.getDistance();
            float visibleLength = Math.max(0f, length - startOffset);
            float halfLength = visibleLength / 2f;
            matrices.translate(0, 0, -(startOffset + halfLength));

            float rollDegrees = (entity.age + tickDelta) * ROLL_DEGREES_PER_TICK;
            matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(rollDegrees));

            MatrixStack.Entry entry = matrices.peek();
            Matrix4f pos = entry.getPositionMatrix();
            Matrix3f norm = entry.getNormalMatrix();
            int stoneBase = entity.getStoneType().getBaseColor();
            int stoneGlint = entity.getStoneType().getGlintColor();

            VertexConsumer innerVc = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            ModRenderHelper.renderBeam(innerVc, pos, norm,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                    stoneBase, 255, HW, HH, halfLength);

            VertexConsumer outerVc = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucentEmissive(TEXTURE));
            ModRenderHelper.renderBeam(outerVc, pos, norm,
                    LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV,
                    stoneGlint, 100, HW * 1.5f, HH * 1.5f, halfLength);
        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    private static Quaternionf rotationFromDirection(Vec3d dir) {
        Vector3f to = new Vector3f((float) dir.x, (float) dir.y, (float) dir.z);
        if (to.lengthSquared() < 1.0e-6f) return new Quaternionf();
        to.normalize();
        return new Quaternionf().rotationTo(MODEL_FORWARD, to);
    }

    @Override
    public Identifier getTexture(EnergyBeamEntity entity) {
        return TEXTURE;
    }
}

package net.alvin.infinityforge.client.render.entity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.entity.PortalEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class PortalEntityRenderer extends EntityRenderer<PortalEntity> {
    private static final Identifier PORTAL_TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/entity/portal.png");

    public PortalEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0f;
    }

    @Override
    public void render(PortalEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider provider, int light) {
        float progress = entity.getAnimationProgress();
        if (progress <= 0f) return;

        matrices.push();
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
            float pitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));

            matrices.translate(0, 1.5f, 0);
            matrices.scale(progress, progress, 1f);
            matrices.translate(0, -1.5f, 0);
            matrices.push();
                VertexConsumer vc = provider.getBuffer(RenderLayer.getEntityTranslucentEmissive(
                        PORTAL_TEXTURE));
                Matrix4f m = matrices.peek().getPositionMatrix();
                Matrix3f n = matrices.peek().getNormalMatrix();
                float hw = 1.0f;
                float hh = 1.5f;

                putVertex(vc, m, n, -hw, 0,    0, 0, 1);
                putVertex(vc, m, n,  hw, 0,    0, 1, 1);
                putVertex(vc, m, n,  hw, hh*2, 0, 1, 0);
                putVertex(vc, m, n, -hw, hh*2, 0, 0, 0);
            matrices.pop();
        matrices.pop();
    }

    private void putVertex(VertexConsumer vc, Matrix4f m, Matrix3f n,
                           float x, float y, float z, float u, float v) {
        vc.vertex(m, x, y, z)
                .color(0f, 0f, 0f, 1f)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(n, 0f, 0f, 1f)
                .next();
    }

    @Override
    public Identifier getTexture(PortalEntity entity) {
        return PORTAL_TEXTURE;
    }
}

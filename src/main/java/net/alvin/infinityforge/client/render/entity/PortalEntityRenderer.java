package net.alvin.infinityforge.client.render.entity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.render.ModRenderHelper;
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

        float hw = entity.getWidth() / 2f;
        float hh = entity.getHeight() / 2f;
        float pitch = MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch());

        matrices.push();
            matrices.translate(0f, hh, 0f);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-yaw));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(pitch));
            matrices.scale(progress, progress, 1f);

            VertexConsumer vc = provider.getBuffer(RenderLayer.getEntityCutoutNoCull(PORTAL_TEXTURE));
            Matrix4f pos = matrices.peek().getPositionMatrix();
            Matrix3f norm = matrices.peek().getNormalMatrix();

            ModRenderHelper.putVertex(vc, pos, norm, -hw, -hh, 0, 0, 1);
            ModRenderHelper.putVertex(vc, pos, norm,  hw, -hh, 0, 1, 1);
            ModRenderHelper.putVertex(vc, pos, norm,  hw,  hh, 0, 1, 0);
            ModRenderHelper.putVertex(vc, pos, norm, -hw,  hh, 0, 0, 0);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(PortalEntity entity) {
        return PORTAL_TEXTURE;
    }
}

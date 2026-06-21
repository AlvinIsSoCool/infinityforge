package net.alvin.infinityforge.client.render.entity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.entity.BlackHoleEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BlackHoleEntityRenderer extends EntityRenderer<BlackHoleEntity> {
    private static final Identifier BLACKHOLE_TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/entity/blackhole.png");
    private static final int LAT_SEGMENTS = 12;
    private static final int LON_SEGMENTS = 16;

    public BlackHoleEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.shadowRadius = 0f;
    }

    @Override
    public void render(BlackHoleEntity entity, float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider provider, int light) {
        float radius = entity.getRadius();
        if (radius <= 0f) return;

        matrices.push();
            matrices.translate(0, radius, 0);

            VertexConsumer vc = provider.getBuffer(RenderLayer.getEntityCutoutNoCull(BLACKHOLE_TEXTURE));
            Matrix4f m = matrices.peek().getPositionMatrix();
            Matrix3f n = matrices.peek().getNormalMatrix();

            for (int lat = 0; lat < LAT_SEGMENTS; lat++) {
                float theta1 = (float) (Math.PI * lat / LAT_SEGMENTS);
                float theta2 = (float) (Math.PI * (lat + 1) / LAT_SEGMENTS);

                for (int lon = 0; lon < LON_SEGMENTS; lon++) {
                    float phi1 = (float) (2 * Math.PI * lon / LON_SEGMENTS);
                    float phi2 = (float) (2 * Math.PI * (lon + 1) / LON_SEGMENTS);

                    Vector3f p1 = sphericalToCartesian(theta1, phi1, radius);
                    Vector3f p2 = sphericalToCartesian(theta2, phi1, radius);
                    Vector3f p3 = sphericalToCartesian(theta2, phi2, radius);
                    Vector3f p4 = sphericalToCartesian(theta1, phi2, radius);

                    putVertex(vc, m, n, p1, 0, 0);
                    putVertex(vc, m, n, p2, 0, 1);
                    putVertex(vc, m, n, p3, 1, 1);
                    putVertex(vc, m, n, p4, 1, 0);
                }
            }
        matrices.pop();
    }

    private Vector3f sphericalToCartesian(float theta, float phi, float radius) {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.cos(theta));
        float z = (float) (radius * Math.sin(theta) * Math.sin(phi));
        return new Vector3f(x, y, z);
    }

    private void putVertex(VertexConsumer vc, Matrix4f m, Matrix3f n,
                           Vector3f pos, float u, float v) {
        vc.vertex(m, pos.x(), pos.y(), pos.z())
                .color(0f, 0f, 0f, 1f)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(LightmapTextureManager.MAX_LIGHT_COORDINATE)
                .normal(n, pos.x(), pos.y(), pos.z())
                .next();
    }

    @Override
    public Identifier getTexture(BlackHoleEntity entity) {
        return BLACKHOLE_TEXTURE;
    }
}

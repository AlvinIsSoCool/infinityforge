package net.alvin.infinityforge.client.render.player;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PlayerForcefieldFeatureRenderer extends
        FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private static final Identifier PLAIN_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/item/stone.png");

    public PlayerForcefieldFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity,
                                PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider provider, int light,
                       AbstractClientPlayerEntity player, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        if (!access.isForcefieldActive()) return;

        boolean isForcefieldHit = access.isForcefieldHit();
        int color = InfinityForgeConfig.get().colorOptions.stoneBaseColors.spaceStone;
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        float pulse = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 0.5 + 0.5);
        float alpha = isForcefieldHit ? 130 / 255f : (40 + 50 * pulse) / 255f;

        VertexConsumer glowVc = provider.getBuffer(RenderLayer.getEntityTranslucentEmissive(PLAIN_TEXTURE));
        this.getContextModel().render(matrices, glowVc, LightmapTextureManager.MAX_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV, r, g, b, alpha);
    }
}
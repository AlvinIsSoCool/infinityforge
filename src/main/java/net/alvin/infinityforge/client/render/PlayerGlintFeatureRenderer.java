package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class PlayerGlintFeatureRenderer extends
        FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public PlayerGlintFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity,
                                PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider provider, int light,
                       AbstractClientPlayerEntity player, float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress, float headYaw, float headPitch) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        if (!access.isForcefieldActive()) return;
        if (!(provider instanceof VertexConsumerProvider.Immediate immediate)) return;

        GlUniform colorUniform = ModRenderLayers.playerGlintShader.getUniform("GlintColor");
        GlUniform timeUniform = ModRenderLayers.playerGlintShader.getUniform("GlintTime");
        GlUniform screenUniform = ModRenderLayers.playerGlintShader.getUniform("ScreenSize");

        boolean wasForcefieldHit = access.isForcefieldHit();
        if (colorUniform != null) {
            int color = wasForcefieldHit
                    ? InfinityForgeConfig.get().colorOptions.stoneGlintColors.spaceStone
                    : InfinityForgeConfig.get().colorOptions.stoneBaseColors.spaceStone;
            colorUniform.set(
                    ((color >> 16) & 0xFF) / 255f,
                    ((color >> 8) & 0xFF) / 255f,
                    (color & 0xFF) / 255f,
                    1f
            );
        }
        if (timeUniform != null)
            timeUniform.set((float)((System.currentTimeMillis() % 100000L) / 1000.0));
        if (screenUniform != null) {
            Window w = MinecraftClient.getInstance().getWindow();
            screenUniform.set((float)w.getFramebufferWidth(), (float)w.getFramebufferHeight());
        }

        VertexConsumer vc = immediate.getBuffer(ModRenderLayers.PLAYER_GLINT);
        this.getContextModel().render(matrices, vc, light, OverlayTexture.DEFAULT_UV,
                1f, 1f, 1f, access.isCustomPhasing() ? 0.25f : 1f);

        immediate.draw(ModRenderLayers.PLAYER_GLINT);
        InfinityForge.LOGGER.info("Render: wasForcefieldHit: {}", wasForcefieldHit);
    }
}
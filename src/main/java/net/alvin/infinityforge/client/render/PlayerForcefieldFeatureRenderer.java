package net.alvin.infinityforge.client.render;

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

        /*if (FabricLoader.getInstance().isModLoaded("iris")) {
            float pulse = (float)(Math.sin(System.currentTimeMillis() / 500.0) * 0.5 + 0.5);
            float alpha = (80 + 175 * pulse) / 255f;

            VertexConsumer glowVc = provider.getBuffer(RenderLayer.getEntityTranslucentEmissive(PLAIN_TEXTURE));
            this.getContextModel().render(matrices, glowVc, light, OverlayTexture.DEFAULT_UV,
                    r, g, b, alpha);
        } else if (provider instanceof VertexConsumerProvider.Immediate immediate) {
            GlUniform colorUniform = ModRenderLayers.playerGlintShader.getUniform("GlintColor");
            GlUniform timeUniform = ModRenderLayers.playerGlintShader.getUniform("GlintTime");
            GlUniform screenUniform = ModRenderLayers.playerGlintShader.getUniform("ScreenSize");

            if (colorUniform != null)
                colorUniform.set(r, g, b, 1f);
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
        } */
    }
}
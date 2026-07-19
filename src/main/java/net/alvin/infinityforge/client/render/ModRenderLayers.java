package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class ModRenderLayers {
    public static ShaderProgram infinityGlintShader;
    public static GlUniform glintColorUniform;
    public static GlUniform glintTimeUniform;
    public static GlUniform glintScreenSizeUniform;
    private static final Identifier GLINT_TEXTURE =
            new Identifier("minecraft", "textures/misc/enchanted_glint_item.png");

    public static final RenderLayer INFINITY_GLINT = RenderLayer.of(
            "infinity_glint",
            VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
            VertexFormat.DrawMode.QUADS,
            256,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(new RenderPhase.ShaderProgram(() -> infinityGlintShader))
                    .texture(new RenderPhase.Texture(
                            GLINT_TEXTURE, true, false))
                    .writeMaskState(RenderPhase.COLOR_MASK)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .depthTest(RenderPhase.EQUAL_DEPTH_TEST)
                    .transparency(RenderPhase.ADDITIVE_TRANSPARENCY)
                    .build(false)
    );

    public static void register() {
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(
                new Identifier(InfinityForge.MOD_ID, "infinity_glint"),
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                shader -> {
                    infinityGlintShader = shader;
                    glintColorUniform = shader.getUniform("GlintColor");
                    glintTimeUniform = shader.getUniform("GlintTime");
                    glintScreenSizeUniform = shader.getUniform("ScreenSize");
                }
        ));
    }
}

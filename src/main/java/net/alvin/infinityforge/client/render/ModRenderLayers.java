package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;
import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class ModRenderLayers {
    public static ShaderProgram stoneGlintShader;
    public static final RenderLayer STONE_GLINT = RenderLayer.of(
            "stone_glint",
            VertexFormats.POSITION,
            VertexFormat.DrawMode.QUADS,
            256,
            RenderLayer.MultiPhaseParameters.builder()
                    .program(new RenderPhase.ShaderProgram(() -> stoneGlintShader))
                    .texture(new RenderPhase.Texture(
                            new Identifier("minecraft", "textures/misc/enchanted_glint_item.png"),
                            true,
                            false
                    ))
                    .writeMaskState(RenderPhase.COLOR_MASK)
                    .cull(RenderPhase.DISABLE_CULLING)
                    .depthTest(RenderPhase.EQUAL_DEPTH_TEST)
                    .transparency(RenderPhase.GLINT_TRANSPARENCY)
                    .build(false)
    );

    public static void initialize() {
        CoreShaderRegistrationCallback.EVENT.register(context -> context.register(
                new Identifier(InfinityForge.MOD_ID, "stone_glint"),
                VertexFormats.POSITION_TEXTURE,
                shader -> stoneGlintShader = shader
        ));
    }
}

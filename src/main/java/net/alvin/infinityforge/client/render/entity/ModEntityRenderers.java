package net.alvin.infinityforge.client.render.entity;

import net.alvin.infinityforge.entity.ModEntities;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class ModEntityRenderers {
    public static void register() {
        EntityRendererRegistry.register(ModEntities.PORTAL_ENTITY, PortalEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.BLACKHOLE_ENTITY, BlackHoleEntityRenderer::new);
    }
}

package net.alvin.infinityforge.client.event;

import net.alvin.infinityforge.client.hud.GauntletHudRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class GauntletHudEvents {
    public static void register() {
        HudRenderCallback.EVENT.register((context, tickDelta) ->
                GauntletHudRenderer.render(context));
    }
}
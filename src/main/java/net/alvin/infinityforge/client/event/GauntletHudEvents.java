package net.alvin.infinityforge.client.event;

import net.alvin.infinityforge.client.hud.GauntletHudRenderer;
import net.alvin.infinityforge.client.hud.TestRendererPassives;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class GauntletHudEvents {
    private static final boolean DEBUG = false;

    public static void register() {
        HudRenderCallback.EVENT.register((context, tickDelta) ->
                GauntletHudRenderer.render(context));

        if (DEBUG) {
            HudRenderCallback.EVENT.register((context, tickDelta) ->
                    TestRendererPassives.render(context));
        }
    }
}
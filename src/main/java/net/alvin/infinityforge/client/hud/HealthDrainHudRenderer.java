package net.alvin.infinityforge.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.entity.effect.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class HealthDrainHudRenderer {
    private static final Identifier HUD_TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/gui/hearts.png");

    private static final int MARGIN_X = 10;
    private static final int MARGIN_Y = 14;
    private static final int SLOT_SIZE = 22;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (!client.player.hasStatusEffect(ModStatusEffects.HEALTH_DRAIN_EFFECT)) return;
        if (client.currentScreen instanceof HandledScreen) return;
        if (client.player.isSpectator()) return;

        renderHealthBar(context, 135, 223, false);
    }

    private static void renderHealthBar(DrawContext context, int x, int y, boolean halfHeart) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        context.drawTexture(HUD_TEXTURE, x, y,
                halfHeart ? 9 : 0, 0, 9, 9, 18, 9);
    }

    private int getHeartCount(LivingEntity entity) {
        if (entity != null && entity.isLiving()) {
            float f = entity.getMaxHealth();
            int i = (int)(f + 0.5F) / 2;
            if (i > 30) {
                i = 30;
            }

            return i;
        } else {
            return 0;
        }
    }

    private int getHeartRows(int heartCount) {
        return (int)Math.ceil(heartCount / 10.0);
    }
}
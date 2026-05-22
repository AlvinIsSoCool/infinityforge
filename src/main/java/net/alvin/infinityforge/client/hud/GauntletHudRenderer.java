package net.alvin.infinityforge.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.helpers.InfinityStoneColors;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.alvin.infinityforge.client.input.GauntletKeybinds.SLOT_KEYS;

public class GauntletHudRenderer {
    private static final Identifier HUD_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/gui/ability_bar.png");

    private static final int MARGIN_X = 10;
    private static final int MARGIN_Y = 14;
    private static final int SLOT_SIZE = 22;
    private static final int SLOT_STEP = 22;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.currentScreen instanceof HandledScreen) return;
        if (client.options.debugEnabled) return;
        if (client.player.isSpectator()) return;

        ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(client.player);
        if (gauntletStack == null) return;

        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);
        List<GauntletAbility> abilities = InfinityGauntletItem.getVisibleAbilities(activeStones);
        if (abilities.isEmpty()) return;

        int scrollOffset = Math.max(0,
                Math.min(GauntletClientState.scrollOffset, Math.max(0, abilities.size() - 6))
        );
        int visibleCount = Math.min(6, abilities.size());

        int startX = MARGIN_X;
        int startY = MARGIN_Y;

        if (abilities.size() > 6) {
            if (scrollOffset > 0)  {
                context.drawTexture(HUD_TEXTURE, startX + 4, startY - 10, 37, 0, 15, 15, 256, 256);
            }

            if (scrollOffset < abilities.size() - 6) {
                context.drawTexture(HUD_TEXTURE, startX + 4, startY + visibleCount * SLOT_STEP + 2, 52, 0, 15, 15, 256, 256);
            }
        }

        boolean isChatScreen = client.currentScreen instanceof ChatScreen;
        long currentTick = client.world != null ? client.world.getTime() : 0L;

        for (int i = 0; i < visibleCount; i++) {
            int abilityIndex = scrollOffset + i;
            GauntletAbility ability = abilities.get(abilityIndex);
            int slotY = startY + i * SLOT_STEP;
            renderAbilitySlot(client.textRenderer, context, ability, startX, slotY, i, isChatScreen, currentTick);
        }
    }

    private static void renderAbilitySlot(TextRenderer textRenderer, DrawContext context,
                                          GauntletAbility ability,
                                          int x, int y, int index,
                                          boolean isChatScreen, long currentTick) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        context.drawTexture(HUD_TEXTURE, x, y, 0, 0, 22, 22, 256, 256);

        int color = ability.getColor();
        if (color == InfinityStoneColors.RAINBOW_ABILITY_COLOR) {
            context.drawTexture(HUD_TEXTURE, x + 1, y + 1, 68, 0, 20, 20, 256, 256);
        } else {
            context.fill(x + 1, y + 1, x + 21, y + 2, color); // top
            context.fill(x + 1, y + 20, x + 21, y + 21, color); // bottom
            context.fill(x + 1, y + 1, x + 2, y + 21, color); // left
            context.fill(x + 20, y + 1, x + 21, y + 21, color); // right
        }

        Identifier iconLocation = ability.getIcon().getIconLocation();
        int iconIndex = ability.getIcon().getIconIndex();
        int u = (iconIndex % 16) * 16;
        int v = (iconIndex / 16) * 16;
        context.drawTexture(iconLocation, x + 3, y + 3, u, v, 16, 16, 256, 256);
        
        if (ability instanceof ActiveAbility) {
            float progress = GauntletClientState.getCooldownProgress(ability.getId(), currentTick);

            if (progress < 1f) {
                int barColor = interpolateColor(progress);
                renderBar(context, x, y, progress, barColor);
            }
        } else if (ability instanceof ToggleAbility || ability instanceof HeldAbility) {
            float progress = GauntletClientState.getChargeProgress(ability.getId());
            if (progress < 1f) {
                int barColor = interpolateColor(progress);
                renderBar(context, x, y, progress, barColor);
            }
        }

        // Toggle indicator
        if (ability instanceof ToggleAbility
                && GauntletClientState.ACTIVE_TOGGLES.contains(ability.getId())) {
            context.drawTexture(HUD_TEXTURE, x + 15, y + 15, 23, 0, 7, 7, 256, 256);
        }

        // Held indicator
        if (ability instanceof HeldAbility
                && GauntletClientState.HELD_ACTIVE.contains(ability.getId())) {
            context.drawTexture(HUD_TEXTURE, x + 15, y + 15, 30, 0, 7, 7, 256, 256);
        }

        if (isChatScreen) {
            context.drawText(
                    textRenderer,
                    ability.getName(),
                    x + SLOT_SIZE + 4,
                    y + 7,
                    0xFFFFFF, true
            );
        } else {
            context.drawText(
                    textRenderer,
                    SLOT_KEYS[index].getBoundKeyLocalizedText().getString(),
                    x + SLOT_SIZE + 4,
                    y + 7,
                    0xFFFFFF, true
            );
        }
    }

    private static void renderBar(DrawContext context, int x, int y, float progress, int color) {
        int barY = y + 17;
        int barMaxWidth = 14;
        int barHeight = 1;
        int barLeftOffset = (SLOT_SIZE - barMaxWidth) / 2;

        // Track
        context.fill(x + barLeftOffset, barY, x + barLeftOffset + barMaxWidth, barY + barHeight, 0xC0333333);

        // Fill
        int filledWidth = (int) (barMaxWidth * progress);
        if (filledWidth > 0)
            context.fill(x + barLeftOffset, barY, x + barLeftOffset + filledWidth, barY + barHeight, color);
    }

    private static int interpolateColor(float progress) {
        // Green at full, yellow at half, red at empty
        int r = (int) (255 * (1f - progress));
        int g = (int) (255 * progress);
        return 0xFF000000 | ((r << 16) | (g << 8));
    }
}
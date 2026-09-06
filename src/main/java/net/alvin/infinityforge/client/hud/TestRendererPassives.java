package net.alvin.infinityforge.client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.state.AbilityDynamicIconState;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.*;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.alvin.infinityforge.client.input.GauntletKeybinds.SLOT_KEYS;

public class TestRendererPassives {
    private static final Identifier HUD_TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/gui/ability_bar.png");

    private static final int MARGIN_X = 255;
    private static final int MARGIN_Y = 14;
    private static final int SLOT_SIZE = 22;

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.currentScreen instanceof HandledScreen) return;
        if (client.options.debugEnabled) return;
        if (client.player.isSpectator()) return;

        ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(client.player);
        if (gauntletStack == null) return;

        List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);
        List<PassiveAbility> abilities = InfinityGauntletItem.getPassiveAbilities(activeStones);
        if (abilities.isEmpty()) return;

        int scrollOffset = Math.max(0,
                Math.min(GauntletClientState.scrollOffset, Math.max(0, abilities.size() - 6)));
        int visibleCount = Math.min(6, abilities.size());

        int startX = MARGIN_X;
        int startY = MARGIN_Y;

        if (abilities.size() > 6) {
            if (scrollOffset > 0)  {
                context.drawTexture(HUD_TEXTURE, startX + 4, startY - 10, 37, 0, 15, 15);
            }

            if (scrollOffset < abilities.size() - 6) {
                context.drawTexture(HUD_TEXTURE, startX + 4, startY + visibleCount * SLOT_SIZE + 2, 52, 0, 15, 15);
            }
        }

        for (int i = 0; i < visibleCount; i++) {
            int abilityIndex = scrollOffset + i;
            GauntletAbility ability = abilities.get(abilityIndex);
            int slotY = startY + i * SLOT_SIZE;
            renderAbilitySlot(client, context, ability, startX, slotY, i);
        }
    }

    private static void renderAbilitySlot(MinecraftClient client, DrawContext context,
                                          GauntletAbility ability,
                                          int x, int y, int index) {
        boolean isChatScreen = client.currentScreen instanceof ChatScreen;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Ability Bar.
        context.drawTexture(HUD_TEXTURE, x, y, 0, 0, 22, 22);

        // Icons.
        ItemStack iconStack = ItemStack.EMPTY;
        if (ability instanceof AbilityDynamicIcon<?>)
            iconStack = AbilityDynamicIconState.get(ability.getId());

        if (!iconStack.isEmpty()) {
            context.getMatrices().push();
            context.getMatrices().translate(x + 3, y + 3, 0);
            context.drawItem(iconStack, 0, 0);
            context.getMatrices().pop();
        } else {
            AbilityIcon icon = ability.getIcon();
            Identifier iconLocation = icon.sheetLocation();
            int u = icon.getU();
            int v = icon.getV();
            context.drawTexture(iconLocation, x + 3, y + 3, u, v, 16, 16);
        }

        // Outline.
        int color = ability.getARGBColor();
        if (color == 0xFF7FFFFF) {
            context.drawTexture(HUD_TEXTURE, x + 1, y + 1, 68, 0, 20, 20);
        } else {
            context.fill(x + 1, y + 1, x + 21, y + 2, color); // top
            context.fill(x + 1, y + 20, x + 21, y + 21, color); // bottom
            context.fill(x + 1, y + 1, x + 2, y + 21, color); // left
            context.fill(x + 20, y + 1, x + 21, y + 21, color); // right
        }

        if (isChatScreen) {
            context.drawText(
                    client.textRenderer,
                    ability.getName(),
                    x + SLOT_SIZE + 4,
                    y + 7,
                    0xFFFFFF, true
            );
        } else {
            context.drawText(
                    client.textRenderer,
                    SLOT_KEYS[index].getBoundKeyLocalizedText().getString(),
                    x + SLOT_SIZE + 4,
                    y + 7,
                    0xFFFFFF, true
            );
        }
    }
}
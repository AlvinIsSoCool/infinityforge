package net.alvin.infinityforge.client.hud;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.ActiveAbility;
import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.abilities.HeldAbility;
import net.alvin.infinityforge.abilities.ToggleAbility;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public class GauntletHudRenderer {
    private static final Identifier HUD_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/gui/ability_bar.png");

    public static void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.currentScreen != null) return;

        ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(client.player);
        if (gauntletStack == null) return;

        InfinityGauntletItem gauntlet = (InfinityGauntletItem) gauntletStack.getItem();
        List<ActiveAbility> abilities = gauntlet.getActiveAbilities(gauntletStack);
        if (abilities.isEmpty()) return;

        int scrollOffset = GauntletClientState.scrollOffset;
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        int visibleCount = Math.min(6, abilities.size());
        int startX = (screenWidth - visibleCount * 20) / 2;
        int startY = screenHeight - 55;

        for (int i = 0; i < visibleCount; i++) {
            int abilityIndex = scrollOffset + i;
            GauntletAbility ability = abilities.get(abilityIndex);
            int slotY = startY + i * 20;
            renderAbilitySlot(context, ability, startX, slotY, i);
        }

        // Scroll indicators — only shown while sneaking since that's
        // the only time scrolling is possible
        if (abilities.size() > 6 && client.player.isSneaking()) {
            boolean canScrollUp = scrollOffset > 0;
            boolean canScrollDown = scrollOffset < abilities.size() - 6;

            int hintX = startX + visibleCount * 20 + 4;
            int hintY = startY + 4;

            if (canScrollUp)
                context.drawText(client.textRenderer, "▲", hintX, hintY, 0xFFFFFF, true);
            if (canScrollDown)
                context.drawText(client.textRenderer, "▼", hintX, hintY + 10, 0xFFFFFF, true);
        }
    }

    private static void renderAbilitySlot(DrawContext context, GauntletAbility ability,
                                          int x, int y, int visualSlot) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Neutral slot background — always drawn
        context.drawTexture(HUD_TEXTURE, x, y, 0, 0, 18, 18, 256, 256);

        // Colored border from ability color
        int color = ability.getColor();
        context.fill(x, y, x + 18, y + 1, color);
        context.fill(x, y + 17, x + 18, y + 18, color);
        context.fill(x, y, x + 1, y + 18, color);
        context.fill(x + 17, y, x + 18, y + 18, color);

        // Ability icon
        Identifier icon = ability.getIcon();
        if (icon != null)
            context.drawTexture(icon, x + 1, y + 1, 0, 0, 16, 16, 16, 16);

        // Toggle indicator — drawn in bottom right corner when active
        if (ability instanceof ToggleAbility
                && GauntletClientState.activeToggles.contains(ability.getId())) {
            // u=18, v=0 — the toggle checkmark region in the sheet
            context.drawTexture(HUD_TEXTURE, x + 10, y + 10, 18, 0, 8, 8, 256, 256);
        }

        // Held indicator — drawn in bottom right corner while key is held
        if (ability instanceof HeldAbility
                && GauntletClientState.heldActive.contains(ability.getId())) {
            // u=26, v=0 — the held indicator region in the sheet
            context.drawTexture(HUD_TEXTURE, x + 10, y + 10, 26, 0, 8, 8, 256, 256);
        }

        // Key number to the right of the slot
        context.drawText(
                client.textRenderer,
                String.valueOf(visualSlot + 1),
                x + 20, y + 5,
                0xFFFFFF, true
        );
    }
}

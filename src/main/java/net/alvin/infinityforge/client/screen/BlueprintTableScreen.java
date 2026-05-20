package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.screen.BlueprintTableScreenHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlueprintTableScreen extends HandledScreen<BlueprintTableScreenHandler> {
    private static final Identifier GUI_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/gui/blueprint_table.png");

    public BlueprintTableScreen(BlueprintTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        titleY += 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(
                GUI_TEXTURE,
                x, y,
                0, 0,
                backgroundWidth, backgroundHeight
        );

        int progress = handler.getScaledProgress();
        int craftingState = handler.getCraftingState();

        if (craftingState == 2) {
            context.drawTexture(
                    GUI_TEXTURE,
                    x + 97, y + 38,
                    176, 0,
                    28, 21
            );
        } else if (craftingState == 1) {
            context.drawTexture(
                    GUI_TEXTURE,
                    x + 100, y + 41,
                    176, 21,
                    progress, 15
            );
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}

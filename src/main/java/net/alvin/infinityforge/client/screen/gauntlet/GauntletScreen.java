package net.alvin.infinityforge.client.screen.gauntlet;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alvin.infinityforge.InfinityForge;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class GauntletScreen extends HandledScreen<GauntletScreenHandler> {
    private static final Identifier GUI_TEXTURE = new Identifier(InfinityForge.MOD_ID, "textures/gui/gauntlet_gui.png");

    public GauntletScreen(GauntletScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 210;
        backgroundWidth = 175;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);

        context.drawTexture(
                GUI_TEXTURE,
                x, y,
                0, 0,
                backgroundWidth, backgroundHeight
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}

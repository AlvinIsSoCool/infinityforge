package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.client.state.ClientKnownDimensionsState;
import net.alvin.infinityforge.network.c2s.OpenPortalC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class PortalScreen extends Screen {
    private static final Identifier SCREEN_TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/gui/portal_screen.png");
    private static final int WIDTH = 248;
    private static final int HEIGHT = 155;

    private TextFieldWidget xField, yField, zField, dimensionField;
    private ButtonWidget openButton;
    private int panelX, panelY;

    public PortalScreen() {
        super(Text.literal("Portal Screen"));
    }

    @Override
    protected void init() {
        panelX = (this.width  - WIDTH) / 2;
        panelY = (this.height - HEIGHT) / 2;
        int fieldX = panelX + 38;
        int fieldW = WIDTH - 50;

        xField = new TextFieldWidget(this.textRenderer, fieldX, panelY + 16, fieldW, 18, Text.literal("X"));
        xField.setMaxLength(10);
        xField.setChangedListener(s -> onFieldChanged());

        yField = new TextFieldWidget(this.textRenderer, fieldX, panelY + 41, fieldW, 18, Text.literal("Y"));
        yField.setMaxLength(10);
        yField.setChangedListener(s -> onFieldChanged());

        zField = new TextFieldWidget(this.textRenderer, fieldX, panelY + 66, fieldW, 18, Text.literal("Z"));
        zField.setMaxLength(10);
        zField.setChangedListener(s -> onFieldChanged());

        dimensionField = new TextFieldWidget(this.textRenderer, fieldX, panelY + 91, fieldW, 18, Text.literal("Dimension"));
        dimensionField.setMaxLength(64);
        dimensionField.setChangedListener(s -> onFieldChanged());

        openButton = ButtonWidget.builder(Text.literal("Open Portal"), this::onPress)
                .dimensions(panelX + (WIDTH - 80) / 2, panelY + 121, 80, 20)
                .build();
        openButton.active = false;

        this.addDrawableChild(xField);
        this.addDrawableChild(yField);
        this.addDrawableChild(zField);
        this.addDrawableChild(dimensionField);
        this.addDrawableChild(openButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        context.drawTexture(SCREEN_TEXTURE, panelX, panelY, 0, 0, WIDTH, HEIGHT);

        int labelX = panelX + 12;
        context.drawTextWithShadow(this.textRenderer, Text.literal("X:"),   labelX, panelY + 20, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Y:"),   labelX, panelY + 45, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Z:"),   labelX, panelY + 70, 0xFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.literal("Dim:"), labelX, panelY + 95, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private void onPress(ButtonWidget btn) {
        int x = Integer.parseInt(xField.getText().trim());
        int y = Integer.parseInt(yField.getText().trim());
        int z = Integer.parseInt(zField.getText().trim());
        Identifier dimId = parseDimensionIdentifier(dimensionField.getText().trim());
        ClientPlayNetworking.send(new OpenPortalC2SPacket(x, y, z, dimId));
        this.close();
    }

    private void onFieldChanged() {
        boolean valid = true;

        for (TextFieldWidget field : new TextFieldWidget[]{xField, yField, zField}) {
            try {
                Integer.parseInt(field.getText().trim());
                field.setEditableColor(0xFFFFFF);
            } catch (NumberFormatException e) {
                field.setEditableColor(0xFF5555);
                valid = false;
            }
        }

        if (isValidDimension(dimensionField.getText().trim())) {
            dimensionField.setEditableColor(0xFFFFFF);
        } else {
            dimensionField.setEditableColor(0xFF5555);
            valid = false;
        }

        openButton.active = valid;
    }

    public static boolean isValidDimension(String dimId) {
        try {
            return switch (Integer.parseInt(dimId)) {
                case 0  -> ClientKnownDimensionsState.exists(new Identifier("minecraft", "overworld"));
                case -1 -> ClientKnownDimensionsState.exists(new Identifier("minecraft", "the_nether"));
                case 1  -> ClientKnownDimensionsState.exists(new Identifier("minecraft", "the_end"));
                default -> false;
            };
        } catch (NumberFormatException ignored) {}

        Identifier id = Identifier.tryParse(dimId);
        return id != null && ClientKnownDimensionsState.exists(id);
    }

    public static Identifier parseDimensionIdentifier(String dimId) {
        try {
            return switch (Integer.parseInt(dimId)) {
                case 0  -> new Identifier("minecraft", "overworld");
                case -1 -> new Identifier("minecraft", "the_nether");
                case 1  -> new Identifier("minecraft", "the_end");
                default -> throw new IllegalArgumentException();
            };
        } catch (NumberFormatException ignored) {}
        return Identifier.tryParse(dimId);
    }
}

package net.alvin.infinityforge.compat.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.item.BlueprintItem;
import net.alvin.infinityforge.block.ModBlocks;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BlueprintCategory implements DisplayCategory<BlueprintDisplay> {
    public static final CategoryIdentifier<BlueprintDisplay> ID =
            CategoryIdentifier.of(InfinityForge.MOD_ID, "blueprint_table");

    @Override
    public CategoryIdentifier<BlueprintDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public Text getTitle() {
        return Text.translatable("gui.infinityforge.blueprint_table");
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModBlocks.BLUEPRINT_TABLE);
    }

    @Override
    public List<Widget> setupDisplay(BlueprintDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        Point origin = bounds.getLocation();

        widgets.add(Widgets.createRecipeBase(bounds));

        widgets.add(Widgets.createSlot(new Point(origin.x + 20, origin.y + 20))
                .entry(display.getInputEntries().get(0).get(0))); // Ingredient
        widgets.add(Widgets.createSlot(new Point(origin.x + 44, origin.y + 20))
                .entry(display.getInputEntries().get(1).get(0))); // Blueprint
        widgets.add(Widgets.createArrow(new Point(origin.x + 72, origin.y + 18))); // Arrow
        widgets.add(Widgets.createSlot(new Point(origin.x + 106, origin.y + 20))
                .entry(display.getOutputEntries().get(0).get(0))
                .markOutput()); // Output

        int craftingTime = BlueprintItem.getCraftingTime(display.getBlueprintStack()) / 20;
        String craftingTimeStr = craftingTime >= 60
                ? String.format("%dm %ds", craftingTime / 60, craftingTime % 60)
                : String.format("%ds", craftingTime);

        widgets.add(Widgets.createLabel(
                new Point(bounds.getMaxX() - 9, bounds.getMaxY() - 14),
                Text.literal("Crafting Time: " + craftingTimeStr)
        ).color(0xFF555555).noShadow().rightAligned());

        return widgets;
    }

    @Override
    public int getDisplayHeight() { return 60; }
}

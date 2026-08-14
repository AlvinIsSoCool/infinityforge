package net.alvin.infinityforge.infinity.abilities.icon;

import net.minecraft.util.Identifier;

/**
 * Represents a single ability icon: a texture sheet plus the local index
 * of a 16x16 tile within a 16-column grid on that sheet.
 * The sheet is an icon atlas of size 256x256.
 *
 * @param sheetLocation The texture sheet this icon is drawn from.
 * @param localIndex    The tile index within the sheet (0-255).
 */
public record AbilityIcon(Identifier sheetLocation, int localIndex) {
    private static final int SHEET_COLUMNS = 16;
    private static final int TILE_SIZE = 16;

    /** @return the x-offset (in pixels) of this icon's tile on its sheet. */
    public int getU() { return (localIndex % SHEET_COLUMNS) * TILE_SIZE; }

    /** @return the y-offset (in pixels) of this icon's tile on its sheet. */
    public int getV() { return (localIndex / SHEET_COLUMNS) * TILE_SIZE; }
}
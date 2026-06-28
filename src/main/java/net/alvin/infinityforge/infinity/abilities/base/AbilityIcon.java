package net.alvin.infinityforge.infinity.abilities.base;

import net.minecraft.util.Identifier;
import java.util.List;

/**
 * This class provides all static icon info for abilities.
 * Maintain index 254 in the png atlas/atlases provided as an empty slot
 * and 255 for a missingno texture of your choosing.
 * As a good practice, use {@link AbilityIcon#empty()} for abilities that use dynamic icons.
 */
public final class AbilityIcon {
    private final Identifier sheetLocation;
    private final int localIndex;
    private static final int ICONS_PER_SHEET = 256;
    private static final Allocator GLOBAL = new Allocator(List.of(), 0);

    private AbilityIcon(Identifier sheetLocation, int localIndex) {
        this.sheetLocation = sheetLocation;
        this.localIndex = localIndex;
    }

    /**
     * Creates a fixed new instance of AbilityIcon without the allocator.
     * @param sheet The sheet to set.
     * @param index The starting index.
     * @return A fixed new AbilityIcon instance.
     * @implNote Please note that this function is not to be used directly,
     *           and is only needed, if you don't want the allocator. This
     *           only supports one sheet and local indices (0-255).
     */
    public static AbilityIcon of(Identifier sheet, int index) { return new AbilityIcon(sheet, index); }

    public static void setSheets(List<Identifier> sheets) { GLOBAL.reset(sheets, 0); }
    public static void setSheet(Identifier sheet) { setSheets(List.of(sheet)); }
    public static AbilityIcon next() { return GLOBAL.next(); }
    public static AbilityIcon peek(int index) { return GLOBAL.peek(index); }
    public static AbilityIcon takeAt(int index) { return GLOBAL.takeAt(index); }
    public static AbilityIcon empty() { return AbilityIcon.peek(254); }
    public static AbilityIcon missingno() { return AbilityIcon.peek(255); }

    public Identifier getIconLocation() { return sheetLocation; }
    public int getIconIndex() { return localIndex; }

    /**
     * This class provides a way to allocate more than one AbilityIcon instances.
     * Third-party mods should instantiate their own {@link Allocator}
     * with their own sheets rather than relying on the global state, which is reserved for the main mod.
     */
    public static final class Allocator {
        private List<Identifier> sheets;
        private int nextIndex;

        public Allocator(List<Identifier> sheets, int startIndex) {
            this.sheets = List.copyOf(sheets);
            this.nextIndex = startIndex;
        }

        public Allocator(Identifier singleSheet, int startIndex) {
            this(List.of(singleSheet), startIndex);
        }

        /**
         * Resets the sheets and the starting index to a specified index.
         * @param sheets The new sheets to set the instance to hold
         * @param startIndex The index from which subsequent methods will read from.
         * @implNote Usually used to set the sheets and set starting index to 0 during instantiation.
         */
        public void reset(List<Identifier> sheets, int startIndex) {
            this.sheets = List.copyOf(sheets);
            this.nextIndex = startIndex;
        }

        /**
         * This function provides the AbilityIcon with the current index
         * and increments the index automatically.
         * @return An instance of the next AbilityIcon from the rolling index.
         * @throws IllegalStateException if there are no more indices left on
         *                               the sheet or list of sheets specified.
         */
        public AbilityIcon next() {
            int totalSlots = sheets.size() * ICONS_PER_SHEET;
            if (nextIndex >= totalSlots) throw new IllegalStateException("No more icon slots left!");
            int sheetIdx = nextIndex / ICONS_PER_SHEET;
            int local = nextIndex % ICONS_PER_SHEET;
            Identifier sheet = sheets.get(sheetIdx);
            nextIndex++;
            return new AbilityIcon(sheet, local);
        }

        /**
         * This provides the AbilityIcon at a particular index
         * without changing the order of icon registration.
         * @param index The index of the icon you want to retrieve.
         * @return An instance of the AbilityIcon at the specified index.
         * @throws IndexOutOfBoundsException if the specified index is out-of-bounds
         *                                   of the sheet or list of sheets.
         */
        public AbilityIcon peek(int index) {
            int totalSlots = sheets.size() * ICONS_PER_SHEET;
            if (index < 0 || index >= totalSlots) throw new IndexOutOfBoundsException(index);
            int sheetIdx = index / ICONS_PER_SHEET;
            int local = index % ICONS_PER_SHEET;
            return new AbilityIcon(sheets.get(sheetIdx), local);
        }

        /**
         * This provides the AbilityIcon at a particular index
         * and moves to that index for all future operations on this instance.
         * @param index The index of the icon you want to retrieve.
         * @return An instance of the AbilityIcon at the specified index.
         * @throws IndexOutOfBoundsException if the specified index is out-of-bounds
         *                                   of the sheet or list of sheets.
         */
        public AbilityIcon takeAt(int index) {
            int totalSlots = sheets.size() * ICONS_PER_SHEET;
            if (index < 0 || index >= totalSlots) throw new IndexOutOfBoundsException(index);
            int sheetIdx = index / ICONS_PER_SHEET;
            int local = index % ICONS_PER_SHEET;
            this.nextIndex = index + 1;
            return new AbilityIcon(sheets.get(sheetIdx), local);
        }

        public AbilityIcon empty() { return peek(254); }
        public AbilityIcon missingno() { return peek(255); }
    }
}

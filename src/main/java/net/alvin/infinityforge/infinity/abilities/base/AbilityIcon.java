package net.alvin.infinityforge.infinity.abilities.base;

import net.minecraft.util.Identifier;
import java.util.List;

/**
 * This class provides all icon info for abilities.
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

    public static void setSheets(List<Identifier> sheets) { GLOBAL.reset(sheets, 0); }
    public static void setSheet(Identifier sheet) { setSheets(List.of(sheet)); }
    public static AbilityIcon next() { return GLOBAL.next(); }
    public static AbilityIcon of(Identifier sheet, int index) { return new AbilityIcon(sheet, index); }

    public Identifier getIconLocation() { return sheetLocation; }
    public int getIconIndex() { return localIndex; }

    /**
     * This class provides a way to allocate more than one AbilityIcon instances.
     */
    public static final class Allocator {
        private List<Identifier> sheets;
        private int nextIndex;

        public Allocator(List<Identifier> sheets, int startIndex) {
            this.sheets = List.copyOf(sheets);
            this.nextIndex = startIndex;
        }

        public Allocator(Identifier singleSheet, int startIndex) { this(List.of(singleSheet), startIndex); }

        public void reset(List<Identifier> sheets, int startIndex) {
            this.sheets = List.copyOf(sheets);
            this.nextIndex = startIndex;
        }

        /**
         * This function allows ability icons to be registered in any order.
         * AbilityIcon.next() will provide the AbilityIcon with the next index.
         * @return An instance of the next AbilityIcon from the rolling index.
         * @throws IllegalStateException is thrown, if there are no more indices left on
         * the sheet or list of sheets specified.
         */
        public AbilityIcon next() {
            int totalSlots = sheets.size() * ICONS_PER_SHEET;
            if (nextIndex >= totalSlots) {
                throw new IllegalStateException("No more icon slots left!");
            }
            int sheetIdx = nextIndex / ICONS_PER_SHEET;
            int local = nextIndex % ICONS_PER_SHEET;
            Identifier sheet = sheets.get(sheetIdx);
            nextIndex++;
            return new AbilityIcon(sheet, local);
        }
    }
}

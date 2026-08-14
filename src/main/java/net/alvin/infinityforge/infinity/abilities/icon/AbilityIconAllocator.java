package net.alvin.infinityforge.infinity.abilities.icon;

import net.minecraft.util.Identifier;

/**
 * Allocates sequential {@link AbilityIcon} slots on a single texture sheet.
 * Indices 254 and 255 are reserved for {@link #empty()} and {@link #missingno()}
 * respectively, and are skipped by {@link #next()}.
 * <p>Each instance tracks its own cursor; create one allocator per sheet.</p>
 * @implNote Classes implementing {@link net.alvin.infinityforge.infinity.abilities.base.AbilityDynamicIcon}
 *           should prefer using {@link #empty()} to avoid static icon registration
 *           and not use {@link #next()} and feed that into the ability class.
 */
public final class AbilityIconAllocator {
    private static final int EMPTY_INDEX = 254;
    private static final int MISSINGNO_INDEX = 255;

    private final Identifier sheet;
    private final AbilityIcon emptyIcon;
    private final AbilityIcon missingnoIcon;
    private int cursor = 0;

    public AbilityIconAllocator(Identifier sheet) {
        this.sheet = sheet;
        this.emptyIcon = new AbilityIcon(sheet, EMPTY_INDEX);
        this.missingnoIcon = new AbilityIcon(sheet, MISSINGNO_INDEX);
    }

    /**
     * @return the next available icon slot on this allocator's sheet,
     *         skipping reserved indices.
     * @throws IndexOutOfBoundsException if all 256 slots have been allocated.
     */
    public AbilityIcon next() {
        while (cursor == EMPTY_INDEX || cursor == MISSINGNO_INDEX) {
            cursor++;
        }
        if (cursor > 255) {
            throw new IndexOutOfBoundsException("No more icon slots left on sheet: " + sheet);
        }
        return new AbilityIcon(sheet, cursor++);
    }

    /** @return the reserved "empty slot" icon for this allocator's sheet. */
    public AbilityIcon empty() { return emptyIcon; }
    /** @return the reserved "missingno" fallback icon for this allocator's sheet. */
    public AbilityIcon missingno() { return missingnoIcon; }
}

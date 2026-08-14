package net.alvin.infinityforge.infinity.abilities.icon;

import net.alvin.infinityforge.InfinityForge;
import net.minecraft.util.Identifier;

public final class ModAbilityIcons {
    public static final Identifier ICON_SHEET = new Identifier(InfinityForge.MOD_ID,
            "textures/gui/ability_icons.png");
    public static final Identifier PASSIVE_ICON_SHEET = new Identifier(InfinityForge.MOD_ID,
            "textures/gui/passive_ability_icons.png");
    private static final AbilityIconAllocator ICONS = new AbilityIconAllocator(ICON_SHEET);
    private static final AbilityIconAllocator PASSIVE_ICONS = new AbilityIconAllocator(PASSIVE_ICON_SHEET);

    //public static final AbilityIcon ENERGY_BLAST = ICONS.next();
    //public static final AbilityIcon ENERGY_BEAM = ICONS.next();
    //public static final AbilityIcon EXPLOSION = ICONS.next();
    public static final AbilityIcon TELEPORT = ICONS.next();
    public static final AbilityIcon PORTAL = ICONS.next();
    public static final AbilityIcon BLACKHOLE = ICONS.next();
    public static final AbilityIcon FORCEFIELD = ICONS.next();
    public static final AbilityIcon PHASING = ICONS.next();
    public static final AbilityIcon WEATHER = ICONS.next();
    public static final AbilityIcon INVISIBILITY = ICONS.next();
    public static final AbilityIcon SIZE_CHANGE_SMALL = ICONS.next();
    public static final AbilityIcon SIZE_CHANGE_BIG = ICONS.next();
    public static final AbilityIcon TURN_INTO_BUBBLES = ICONS.next();
    public static final AbilityIcon KILL = ICONS.next();
    public static final AbilityIcon SNAP = ICONS.next();
    //public static final AbilityIcon FLIGHT = ICONS.next();
    //public static final AbilityIcon TELEKINESIS = ICONS.next();
    //public static final AbilityIcon CHANGE_SNAP = ICONS.next();
    //public static final AbilityIcon ADVANCE_TIME = ICONS.next();
    //public static final AbilityIcon REWIND_TIME = ICONS.next();
    //public static final AbilityIcon STOP_TIME = ICONS.next();
    //public static final AbilityIcon RANDOMISE_TIME = ICONS.next();

    public static final AbilityIcon KNOCKBACK_RESISTANCE = PASSIVE_ICONS.next();
    public static final AbilityIcon SPEED = PASSIVE_ICONS.next();
    public static final AbilityIcon ATTACK_SPEED = PASSIVE_ICONS.next();
    public static final AbilityIcon STEP_HEIGHT = PASSIVE_ICONS.next();
    public static final AbilityIcon HEALTH = PASSIVE_ICONS.next();
    public static final AbilityIcon HEALING = PASSIVE_ICONS.next();
    public static final AbilityIcon SATURATION = PASSIVE_ICONS.next();
    public static final AbilityIcon WATER_BREATHING = PASSIVE_ICONS.next();

    public static AbilityIcon empty() { return ICONS.empty(); }
    public static AbilityIcon missingno() { return ICONS.missingno(); }
    public static AbilityIcon emptyPassive() { return PASSIVE_ICONS.empty(); }
    public static AbilityIcon missingnoPassive() { return PASSIVE_ICONS.missingno(); }

    private ModAbilityIcons() { throw new AssertionError("No instances"); }
}

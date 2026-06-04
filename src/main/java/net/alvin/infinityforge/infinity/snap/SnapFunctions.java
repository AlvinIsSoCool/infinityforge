package net.alvin.infinityforge.infinity.snap;

public enum SnapFunctions {
    KILL_HALF("snapfunctions.infinityforge.kill_half"),
    KILL_ALL("snapfunctions.infinityforge.kill_all"),
    KILL_HOSTILES("snapfunctions.infinityforge.kill_hostiles"),
    REVERT_KILLS("snapfunctions.infinityforge.revert_kills"),
    RECREATE_WORLD("snapfunctions.infinityforge.recreate_world"),
    DESTROY_STONES("snapfunctions.infinityforge.destroy_stones"),
    CREATIVE_MODE("snapfunctions.infinityforge.creative_mode"),
    SPECTATOR_MODE("snapfunctions.infinityforge.spectator_mode");

    public final String key;
    SnapFunctions(String key) { this.key = key; }
}

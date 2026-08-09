package net.alvin.infinityforge.util.accessor;

public interface PlayerEffectsAccess {
    float infinityforge$getScale();
    boolean infinityforge$isInvisible();
    boolean infinityforge$isPhasing();
    boolean infinityforge$isForcefieldActive();
    boolean infinityforge$isForcefieldHit();

    void infinityforge$setScale(float scale);
    void infinityforge$setInvisible(boolean invisible);
    void infinityforge$setPhasing(boolean phasing);
    void infinityforge$setForcefieldActive(boolean forcefieldActive);
    void infinityforge$setForcefieldHit(boolean forcefieldHit);
}
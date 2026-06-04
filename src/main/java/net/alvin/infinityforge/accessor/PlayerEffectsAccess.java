package net.alvin.infinityforge.accessor;

public interface PlayerEffectsAccess {
    float getCustomScale();
    boolean isCustomInvisible();
    boolean isCustomPhasing();
    boolean isForcefieldActive();
    boolean isForcefieldHit();

    void setCustomScale(float scale);
    void setCustomInvisible(boolean invisible);
    void setCustomPhasing(boolean phasing);
    void setForcefieldActive(boolean forcefieldActive);
    void setForcefieldHit(boolean forcefieldHit);
}
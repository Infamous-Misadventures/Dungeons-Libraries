package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Use {@link FollowerLeaderHelper} instead.
 * Removal in 1.20.0
 */
@Deprecated(forRemoval = true)
public interface Minion {
    @Nullable LivingEntity getMaster();
    void setMaster(LivingEntity master);
    boolean isMinion();
    boolean isSummon();
    void setSummon(boolean summon);
    int getMinionTimer();
    void setMinionTimer(int minionTimer);
    boolean isTemporary();
    void setTemporary(boolean temporary);
    boolean revertsOnExpiration();
    void setRevertsOnExpiration(boolean revertsOnExpiration);
    boolean isGoalsAdded();
    void setGoalsAdded(boolean goalsAdded);
}

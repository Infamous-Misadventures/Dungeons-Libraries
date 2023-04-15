package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
/**
 * @deprecated Use {@link FollowerLeaderHelper} instead.
 * Removal in 1.20.0
 */
@Deprecated(forRemoval = true)
public interface Master {

    void copyFrom(Master summoner);

    List<Entity> getAllMinions();

    List<Entity> getSummonedMobs();

    int getSummonedMobsCost();

    boolean addSummonedMob(Entity entity);

    void setSummonedMobs(List<Entity> summonedMobs);

    boolean addMinion(Entity entity);

    List<Entity> getOtherMinions();

    void setOtherMinions(List<Entity> otherMinions);

    void removeMinion(LivingEntity entityLiving);

}

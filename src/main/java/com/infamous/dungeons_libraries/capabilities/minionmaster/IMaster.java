package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.entity.Entity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.UUID;

public interface IMaster {

    void copyFrom(IMaster summoner);

    List<Entity> getSummonedMobs();

    int getSummonedMobsCost();

    boolean addSummonedMob(Entity entity);

    void setSummonedMobs(List<Entity> summonedMobs);

    void setSummonedMobsUUID(List<UUID> summonedMobsUUID);

    void setLevelOnLoad(ResourceLocation levelOnLoad);
}

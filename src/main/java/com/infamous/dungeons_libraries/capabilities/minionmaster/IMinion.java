package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public interface IMinion {

    LivingEntity getMaster();

    void setMaster(LivingEntity master);

    void setMasterUUID(UUID masterUUID);

    void setLevelOnLoad(ResourceLocation levelOnLoad);

    boolean isMinion();

    boolean isSummon();

    void setSummon(boolean summon);
}

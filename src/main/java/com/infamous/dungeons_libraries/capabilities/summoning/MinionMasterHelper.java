package com.infamous.dungeons_libraries.capabilities.summoning;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.UUID;

public class MinionMasterHelper {

    @Nullable
    public static LivingEntity getOwnerForHorse(AbstractHorseEntity horseEntity){
        try {
            if(horseEntity.getOwnerUUID() != null){
                UUID ownerUniqueId = horseEntity.getOwnerUUID();
                return ownerUniqueId == null ? null : horseEntity.level.getPlayerByUUID(ownerUniqueId);
            }
            else return null;
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static boolean isMinionEntity(Entity target) {
        return target instanceof IronGolemEntity
                || target instanceof WolfEntity
                || target instanceof LlamaEntity
                || target instanceof BatEntity
                || target instanceof BeeEntity
                || target instanceof SheepEntity;
    }

    public static boolean isMinionOf(LivingEntity target, UUID ownerUUID) {
        if(isMinionEntity(target)){
            IMinion targetSummonableCap = getMinionCapability(target);
            if(targetSummonableCap == null){
                return false;
            }
            else{
                return targetSummonableCap.getMaster() != null
                        && targetSummonableCap.getMaster() == ownerUUID;
            }
        } else{
            return false;
        }
    }

    @Nullable
    public static LivingEntity getMaster(LivingEntity minionMob) {
        try {
            IMinion summonable = getMinionCapability(minionMob);
            if(summonable == null) return null;
            if(summonable.getMaster() != null){
                UUID ownerUniqueId = summonable.getMaster();
                return ownerUniqueId == null ? null : minionMob.level.getPlayerByUUID(ownerUniqueId);
            }
            else return null;
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Nullable
    public static IMaster getMasterCapability(Entity entity)
    {
        LazyOptional<IMaster> lazyCap = entity.getCapability(MasterProvider.MASTER_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summoner capability from the Entity!"));
        }
        return null;
    }

    @Nullable
    public static IMinion getMinionCapability(Entity entity)
    {
        LazyOptional<IMinion> lazyCap = entity.getCapability(MinionProvider.MINION_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summonable capability from the Entity!"));
        }
        return null;
    }
}

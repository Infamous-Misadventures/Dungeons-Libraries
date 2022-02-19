package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtByTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MinionFollowOwnerGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
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
            IMinion minion = getMinionCapability(minionMob);
            if(minion == null) return null;
            if(minion.getMaster() != null){
                UUID ownerUniqueId = minion.getMaster();
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

    public static void addMinionGoals(Entity entity) {
        IMinion minionCap = getMinionCapability(entity);
        if(minionCap == null) return;
        if(minionCap.isMinion() && (entity instanceof IronGolemEntity || entity instanceof BeeEntity)){
            MobEntity mobEntity = (MobEntity) entity;
            mobEntity.goalSelector.addGoal(2, new MinionFollowOwnerGoal(mobEntity, 2.1D, 10.0F, 2.0F, false));
            mobEntity.targetSelector.addGoal(1, new MasterHurtByTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(2, new MasterHurtTargetGoal(mobEntity));
        }
    }
}

package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtByTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MinionFollowOwnerGoal;
import com.infamous.dungeons_libraries.entities.ai.goal.MeleeAttackGoal;
import com.infamous.dungeons_libraries.mixin.GoalSelectorAccessor;
import com.infamous.dungeons_libraries.mixin.MobEntityInvoker;
import com.infamous.dungeons_libraries.summon.SummonConfig;
import com.infamous.dungeons_libraries.summon.SummonConfigRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

import static com.infamous.dungeons_libraries.utils.PetHelper.canPetAttackEntity;

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
        IMinion targetSummonableCap = getMinionCapability(target);
        if(targetSummonableCap == null){
            return false;
        }else {
            return targetSummonableCap.getMaster() != null;
        }
    }

    public static boolean isMinionOf(LivingEntity target, LivingEntity owner) {
        IMinion targetSummonableCap = getMinionCapability(target);
        if(targetSummonableCap == null){
            return false;
        } else{
            return targetSummonableCap.getMaster() != null
                    && targetSummonableCap.getMaster() == owner;
        }
    }

    @Nullable
    public static LivingEntity getMaster(LivingEntity minionMob) {
        try {
            IMinion minion = getMinionCapability(minionMob);
            if(minion == null) return null;
            return minion.getMaster();
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

    public static void addMinionGoals(MobEntity mobEntity) {
        IMinion minionCap = getMinionCapability(mobEntity);
        if(minionCap == null) return;
        if(minionCap.isMinion()){
            mobEntity.goalSelector.addGoal(2, new MinionFollowOwnerGoal(mobEntity, 2.1D, 10.0F, 2.0F, false));
            clearGoals(mobEntity.targetSelector);
            mobEntity.targetSelector.addGoal(1, new MasterHurtByTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(2, new MasterHurtTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class, 5, false, false,
                    (entityIterator) -> canPetAttackEntity(mobEntity, entityIterator)));
        }
        if(minionCap.isSummon()){
            SummonConfig config = SummonConfigRegistry.getConfig(mobEntity.getType().getRegistryName());
            if(config.shouldAddAttackGoal()){
                mobEntity.goalSelector.addGoal(1, new MeleeAttackGoal(mobEntity, 1.0D, true));
            }
        }
    }

    static void removeMinion(LivingEntity entityLiving, IMinion cap) {
        LivingEntity master = cap.getMaster();
        IMaster masterCapability = getMasterCapability(master);
        if(masterCapability != null){
            masterCapability.removeMinion(entityLiving);
        }
        cap.setMaster(null);
        if(entityLiving instanceof MobEntity){
            MobEntity mobEntity = (MobEntity) entityLiving;
            clearGoals(mobEntity.goalSelector);
            clearGoals(mobEntity.targetSelector);
            ((MobEntityInvoker) entityLiving).registerGoals();
        }
    }

    private static void clearGoals(GoalSelector goalSelector) {
        ArrayList<PrioritizedGoal> prioritizedGoals = new ArrayList<>(((GoalSelectorAccessor) goalSelector).getAvailableGoals());
        prioritizedGoals.forEach(prioritizedGoal -> goalSelector.removeGoal(prioritizedGoal.getGoal()));
    }
}

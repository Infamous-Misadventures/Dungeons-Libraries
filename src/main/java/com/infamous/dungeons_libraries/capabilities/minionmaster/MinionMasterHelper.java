package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtByTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MasterHurtTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.MinionFollowOwnerGoal;
import com.infamous.dungeons_libraries.entities.ai.goal.MeleeAttackGoal;
import com.infamous.dungeons_libraries.mixin.MobInvoker;
import com.infamous.dungeons_libraries.summon.SummonConfig;
import com.infamous.dungeons_libraries.summon.SummonConfigRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.MASTER_CAPABILITY;
import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.MINION_CAPABILITY;
import static com.infamous.dungeons_libraries.utils.PetHelper.canPetAttackEntity;

public class MinionMasterHelper {

    @Nullable
    public static LivingEntity getOwnerForHorse(AbstractHorse horseEntity){
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
        Minion targetSummonableCap = getMinionCapability(target);
        if(targetSummonableCap == null){
            return false;
        }else {
            return targetSummonableCap.getMaster() != null;
        }
    }

    public static boolean isMinionOf(LivingEntity target, LivingEntity owner) {
        Minion targetSummonableCap = getMinionCapability(target);
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
            Minion minion = getMinionCapability(minionMob);
            if(minion == null) return null;
            return minion.getMaster();
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    @Nullable
    public static Master getMasterCapability(Entity entity)
    {
        LazyOptional<Master> lazyCap = entity.getCapability(MASTER_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summoner capability from the Entity!"));
        }
        return null;
    }

    @Nullable
    public static Minion getMinionCapability(Entity entity)
    {
        LazyOptional<Minion> lazyCap = entity.getCapability(MINION_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summonable capability from the Entity!"));
        }
        return null;
    }

    public static void addMinionGoals(Mob mobEntity) {
        Minion minionCap = getMinionCapability(mobEntity);
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

    static void removeMinion(LivingEntity entityLiving, Minion cap) {
        LivingEntity master = cap.getMaster();
        Master masterCapability = getMasterCapability(master);
        if(masterCapability != null){
            masterCapability.removeMinion(entityLiving);
        }
        cap.setMaster(null);
        if(entityLiving instanceof Mob){
            Mob mobEntity = (Mob) entityLiving;
            clearGoals(mobEntity.goalSelector);
            clearGoals(mobEntity.targetSelector);
            ((MobInvoker) entityLiving).invokeRegisterGoals();
        }
    }

    private static void clearGoals(GoalSelector goalSelector) {
        ArrayList<WrappedGoal> wrappedGoals = new ArrayList<>(goalSelector.getAvailableGoals());
        wrappedGoals.forEach(prioritizedGoal -> goalSelector.removeGoal(prioritizedGoal.getGoal()));
    }
}

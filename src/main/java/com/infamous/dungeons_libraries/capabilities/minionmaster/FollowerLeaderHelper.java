package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.LeaderHurtByTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.LeaderHurtTargetGoal;
import com.infamous.dungeons_libraries.capabilities.minionmaster.goals.FollowerFollowLeaderGoal;
import com.infamous.dungeons_libraries.mixin.MobInvoker;
import com.infamous.dungeons_libraries.summon.SummonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.Monster;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.FOLLOWER_CAPABILITY;
import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.LEADER_CAPABILITY;
import static com.infamous.dungeons_libraries.utils.PetHelper.canPetAttackEntity;

public class FollowerLeaderHelper {

    public static Leader getLeaderCapability(Entity entity) {
        return entity.getCapability(LEADER_CAPABILITY).orElse(new Leader());
    }

    public static Follower getFollowerCapability(Entity entity) {
        return entity.getCapability(FOLLOWER_CAPABILITY).orElse(new Follower());
    }

    @Nullable
    public static LivingEntity getOwnerForHorse(AbstractHorse horseEntity) {
        try {
            if (horseEntity.getOwnerUUID() != null) {
                UUID ownerUniqueId = horseEntity.getOwnerUUID();
                return ownerUniqueId == null ? null : horseEntity.level.getPlayerByUUID(ownerUniqueId);
            } else return null;
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }

    public static boolean isFollower(Entity target) {
        return getFollowerCapability(target).getLeader() != null;
    }

    public static boolean isFollowerOf(LivingEntity target, LivingEntity owner) {
        Follower targetSummonableCap = getFollowerCapability(target);
        return targetSummonableCap.getLeader() != null
                && targetSummonableCap.getLeader() == owner;
    }

    @Nullable
    public static LivingEntity getLeader(LivingEntity minionMob) {
        Follower minion = getFollowerCapability(minionMob);
        return minion.getLeader();
    }

    private void makeFollowerOf(LivingEntity livingEntity, LivingEntity nearbyEntity) {
        if (nearbyEntity instanceof Monster) {
            Monster mobEntity = (Monster) nearbyEntity;
            Leader leaderCapability = FollowerLeaderHelper.getLeaderCapability(livingEntity);
            Follower minionCapability = FollowerLeaderHelper.getFollowerCapability(nearbyEntity);
            leaderCapability.addFollower(mobEntity);
            minionCapability.setLeader(livingEntity);
            minionCapability.setGoalsAdded(false);
            ((Monster) nearbyEntity).setTarget(null);
            addFollowerGoals(mobEntity);
        }
    }

    private void makeTemporaryFollowerOf(LivingEntity livingEntity, LivingEntity nearbyEntity, int followerDuration, boolean revertsOnExpiration) {
        if (nearbyEntity instanceof Mob mob) {
            Leader leaderCapability = FollowerLeaderHelper.getLeaderCapability(livingEntity);
            Follower minionCapability = FollowerLeaderHelper.getFollowerCapability(nearbyEntity);
            leaderCapability.addFollower(mob);
            minionCapability.setLeader(livingEntity);
            minionCapability.setTemporary(true);
            minionCapability.setRevertsOnExpiration(revertsOnExpiration);
            minionCapability.setFollowerDuration(followerDuration);
            minionCapability.setGoalsAdded(false);
            mob.setTarget(null);
            addFollowerGoals(mob);
        }
    }

    public static void addFollowerGoals(Mob mobEntity) {
        Follower minionCap = getFollowerCapability(mobEntity);
        if (minionCap.isGoalsAdded()) return;
        if (minionCap.isFollower()) {
            mobEntity.goalSelector.addGoal(2, new FollowerFollowLeaderGoal(mobEntity, 1.5D, 24.0F, 3.0F, false));
            clearGoals(mobEntity.targetSelector);
            mobEntity.targetSelector.addGoal(1, new LeaderHurtByTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(2, new LeaderHurtTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mobEntity, LivingEntity.class, 5, false, false,
                    (entityIterator) -> entityIterator.getClassification(false).equals(MobCategory.MONSTER) && canPetAttackEntity(mobEntity, entityIterator)));

            minionCap.getLeader().getCapability(ModCapabilities.LEADER_CAPABILITY).ifPresent(leader -> {
                leader.addFollower(mobEntity);
            });
            SummonHelper.addSummonGoals(mobEntity);
            minionCap.setGoalsAdded(true);
        }
    }

    public static void removeFollower(LivingEntity entityLiving) {
        Follower cap = getFollowerCapability(entityLiving);
        LivingEntity leader = cap.getLeader();
        Leader leaderCapability = getLeaderCapability(leader);
        leaderCapability.removeMinion(entityLiving);
        cap.setLeader(null);
        if (entityLiving instanceof Mob) {
            Mob mobEntity = (Mob) entityLiving;
            clearGoals(mobEntity.goalSelector);
            clearGoals(mobEntity.targetSelector);
            cap.setGoalsAdded(false);
            ((MobInvoker) entityLiving).invokeRegisterGoals();
        }
    }

    private static void clearGoals(GoalSelector goalSelector) {
        ArrayList<WrappedGoal> wrappedGoals = new ArrayList<>(goalSelector.getAvailableGoals());
        wrappedGoals.forEach(prioritizedGoal -> goalSelector.removeGoal(prioritizedGoal.getGoal()));
    }
}

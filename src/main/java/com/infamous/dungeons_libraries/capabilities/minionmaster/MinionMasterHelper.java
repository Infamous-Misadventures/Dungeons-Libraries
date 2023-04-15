package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import javax.annotation.Nullable;

/**
 * @deprecated Use {@link FollowerLeaderHelper} instead.
 * Removal in 1.20.0
 */
@Deprecated(forRemoval = true)
public class MinionMasterHelper {

    @Deprecated(forRemoval = true)
    @Nullable
    public static LivingEntity getOwnerForHorse(AbstractHorse horseEntity) {
        return FollowerLeaderHelper.getOwnerForHorse(horseEntity);
    }

    @Deprecated(forRemoval = true)
    public static boolean isMinionEntity(Entity target) {
        return FollowerLeaderHelper.isFollower(target);
    }

    @Deprecated(forRemoval = true)
    public static boolean isMinionOf(LivingEntity target, LivingEntity owner) {
        return FollowerLeaderHelper.isFollowerOf(target, owner);
    }

    @Deprecated(forRemoval = true)
    @Nullable
    public static LivingEntity getMaster(LivingEntity minionMob) {
        return FollowerLeaderHelper.getLeader(minionMob);
    }

    @Deprecated(forRemoval = true)
    public static Master getMasterCapability(Entity entity) {
        return FollowerLeaderHelper.getLeaderCapability(entity);
    }

    @Deprecated(forRemoval = true)
    public static Minion getMinionCapability(Entity entity) {
        return FollowerLeaderHelper.getFollowerCapability(entity);
    }

    @Deprecated(forRemoval = true)
    public static void addMinionGoals(Mob mobEntity) {
        FollowerLeaderHelper.addFollowerGoals(mobEntity);
    }

    @Deprecated(forRemoval = true)
    static void removeMinion(LivingEntity entityLiving, Minion cap) {
        FollowerLeaderHelper.removeFollower(entityLiving);
    }

}

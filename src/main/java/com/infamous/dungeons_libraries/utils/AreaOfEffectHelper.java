package com.infamous.dungeons_libraries.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class AreaOfEffectHelper {

    public static void applyToNearbyEntities(LivingEntity origin, float distance, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        World world = origin.getCommandSenderWorld();
        applyToNearbyEntities(origin, world, distance, applicablePredicate, entityConsumer);
    }

    public static void applyToNearbyEntities(Entity origin, World world, float distance, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        List<LivingEntity> nearbyEntities = getNearbyEnemies(origin, distance, world, applicablePredicate);
        if (nearbyEntities.isEmpty()) return;
        for (LivingEntity nearbyEntity : nearbyEntities) {
            entityConsumer.accept(nearbyEntity);
        }
    }

    public static void applyToNearbyEntities(LivingEntity origin, float distance, int limit, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        World world = origin.getCommandSenderWorld();
        applyToNearbyEntities(origin, world, distance, limit, applicablePredicate, entityConsumer);
    }

    public static void applyToNearbyEntities(Entity origin, World world, float distance, int limit, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        List<LivingEntity> nearbyEntities = getNearbyEnemies(origin, distance, world, applicablePredicate);
        if (nearbyEntities.isEmpty()) return;
        if (limit > nearbyEntities.size()) limit = nearbyEntities.size();
        for (int i = 0; i < limit; i++) {
            if (nearbyEntities.size() >= i + 1) {
                LivingEntity nearbyEntity = nearbyEntities.get(i);
                entityConsumer.accept(nearbyEntity);
            }
        }
    }

    public static List<LivingEntity> getNearbyEnemies(Entity origin, float distance, World world, Predicate<LivingEntity> applicablePredicate) {
        return world.getLoadedEntitiesOfClass(LivingEntity.class,
                origin.getBoundingBox().inflate(distance),
                applicablePredicate);
    }

    public static void applyToNearbyEntitiesAtPos(BlockPos origin, World world, float distance, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        List<LivingEntity> nearbyEntities = getNearbyEnemies(origin, distance, world, applicablePredicate);
        if (nearbyEntities.isEmpty()) return;
        for (LivingEntity nearbyEntity : nearbyEntities) {
            entityConsumer.accept(nearbyEntity);
        }
    }

    public static void applyToNearbyEntitiesAtPos(BlockPos origin, World world, float distance, int limit, Predicate<LivingEntity> applicablePredicate, Consumer<LivingEntity> entityConsumer) {
        List<LivingEntity> nearbyEntities = getNearbyEnemies(origin, distance, world, applicablePredicate);
        if (nearbyEntities.isEmpty()) return;
        if (limit > nearbyEntities.size()) limit = nearbyEntities.size();
        for (int i = 0; i < limit; i++) {
            if (nearbyEntities.size() >= i + 1) {
                LivingEntity nearbyEntity = nearbyEntities.get(i);
                entityConsumer.accept(nearbyEntity);
            }
        }
    }

    public static List<LivingEntity> getNearbyEnemies(BlockPos origin, float distance, World world, Predicate<LivingEntity> applicablePredicate) {
        return world.getLoadedEntitiesOfClass(LivingEntity.class,
                new AxisAlignedBB(origin).inflate(distance),
                applicablePredicate);
    }

    public static Predicate<LivingEntity> getCanApplyToSecondEnemyPredicate(LivingEntity attacker, LivingEntity target) {
        return (nearbyEntity) -> AbilityHelper.canApplyToSecondEnemy(attacker, target, nearbyEntity);
    }

    public static Predicate<LivingEntity> getCanApplyToEnemyPredicate(LivingEntity attacker) {
        return (nearbyEntity) -> AbilityHelper.canApplyToEnemy(attacker, nearbyEntity);
    }

    public static Predicate<LivingEntity> getCanHealPredicate(LivingEntity healer) {
        return (nearbyEntity) -> AbilityHelper.canHealEntity(healer, nearbyEntity);
    }
}

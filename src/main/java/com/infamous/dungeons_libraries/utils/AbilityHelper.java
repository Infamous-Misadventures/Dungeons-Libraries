package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_libraries.utils.PetHelper.isPetOf;
import static net.minecraft.entity.EntityType.ARMOR_STAND;

public class AbilityHelper {

    public static boolean isFacingEntity(Entity looker, Entity target, Vector3d look, int angle) {
        if (angle <= 0) return false;
        Vector3d posVec = target.position().add(0, target.getEyeHeight(), 0);
        Vector3d relativePosVec = posVec.vectorTo(looker.position().add(0, looker.getEyeHeight(), 0)).normalize();
        //relativePosVec = new Vector3d(relativePosVec.x, 0.0D, relativePosVec.z);

        double dotsq = ((relativePosVec.dot(look) * Math.abs(relativePosVec.dot(look))) / (relativePosVec.lengthSqr() * look.lengthSqr()));
        double cos = MathHelper.cos((float) ((angle / 360d) * Math.PI));
        return dotsq < -(cos * cos);
    }

    private static boolean isNotTheTargetOrAttacker(LivingEntity attacker, LivingEntity target, LivingEntity nearbyEntity) {
        return nearbyEntity != target
                && nearbyEntity != attacker;
    }

    private static boolean isNotAPlayerOrCanApplyToPlayers(LivingEntity attacker, LivingEntity nearbyEntity) {
        if(attacker instanceof PlayerEntity) {
            if (!(nearbyEntity instanceof PlayerEntity)) {
                return true;
            } else {
                return DungeonsLibrariesConfig.ENABLE_AREA_OF_EFFECT_ON_OTHER_PLAYERS.get();
            }
        }else{
            return true;
        }
    }

    public static boolean canHealEntity(LivingEntity healer, LivingEntity nearbyEntity) {
        return nearbyEntity != healer
                && isAlly(healer, nearbyEntity)
                && isAliveAndCanBeSeen(nearbyEntity, healer);
    }

    public static boolean isAlly(LivingEntity origin, LivingEntity nearbyEntity) {
        return isPetOf(origin, nearbyEntity)
                || origin.isAlliedTo(nearbyEntity)
                || (origin instanceof MonsterEntity && nearbyEntity instanceof MonsterEntity);
    }

    private static boolean isEntityBlacklisted(LivingEntity entity) {
        return (ForgeRegistries.ENTITIES.getKey(entity.getType()) != null && (DungeonsLibrariesConfig.ENEMY_BLACKLIST.get().contains(ForgeRegistries.ENTITIES.getKey(entity.getType()).toString()) || entity.getType().equals(ARMOR_STAND)));
    }

    private static boolean isAliveAndCanBeSeen(LivingEntity nearbyEntity, LivingEntity attacker) {
        return nearbyEntity.isAlive() && attacker.canSee(nearbyEntity);
    }

    public static boolean canApplyToSecondEnemy(LivingEntity attacker, LivingEntity target, LivingEntity nearbyEntity) {
        return isNotTheTargetOrAttacker(attacker, target, nearbyEntity)
                && isAliveAndCanBeSeen(nearbyEntity, attacker)
                && !isAlly(attacker, nearbyEntity)
                && isNotAPlayerOrCanApplyToPlayers(attacker, nearbyEntity)
                && !isEntityBlacklisted(nearbyEntity);
    }

    public static boolean canApplyToEnemy(LivingEntity attacker, LivingEntity nearbyEntity) {
        return nearbyEntity != attacker
                && isAliveAndCanBeSeen(nearbyEntity, attacker)
                && !isAlly(attacker, nearbyEntity)
                && isNotAPlayerOrCanApplyToPlayers(attacker, nearbyEntity)
                && !isEntityBlacklisted(nearbyEntity);
    }

}

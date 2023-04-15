package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Follower;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Leader;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Master;
import com.infamous.dungeons_libraries.entities.ai.goal.MeleeAttackGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SUMMON_CAP;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.*;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;

public class SummonHelper {

    private static boolean addSummonedMob(LivingEntity master, Entity entity) {
        Leader leaderCapability = getLeaderCapability(master);
        if (canSummonMob(master, entity, leaderCapability)) {
            return leaderCapability.addSummonedMob(entity);
        }
        return false;
    }

    private static boolean canSummonMob(LivingEntity leader, Entity beeEntity, Leader leaderCap) {
        AttributeInstance summonCapAttribute = leader.getAttribute(SUMMON_CAP.get());
        if (summonCapAttribute == null) return false;
        return leaderCap.getSummonedMobsCost() + SummonConfigRegistry.getConfig(ForgeRegistries.ENTITY_TYPES.getKey(beeEntity.getType())).getCost() <= summonCapAttribute.getValue();
    }

    public static boolean canSummonMob(LivingEntity master, Master masterCap) {
        return canSummonMob(master, (Leader) masterCap);
    }

    public static boolean canSummonMob(LivingEntity master, Leader leaderCap) {
        AttributeInstance summonCostLimitAttribute = master.getAttribute(SUMMON_CAP.get());
        if (summonCostLimitAttribute == null) return false;
        return leaderCap.getSummonedMobsCost() < summonCostLimitAttribute.getValue();
    }

    public static Entity summonEntity(LivingEntity leader, BlockPos position, EntityType<?> entityType) {
        Entity entity = entityType.create(leader.level);
        if (entity != null) {
            Follower summonable = getFollowerCapability(entity);
            if (addSummonedMob(leader, entity)) {
                summonable.setLeader(leader);
                summonable.setSummon(true);
                createSummon(leader, entity, position);
                return entity;
            } else {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        return null;
    }

    private static void createSummon(LivingEntity master, Entity entity, BlockPos position) {
        entity.moveTo((double) position.getX() + 0.5D, (double) position.getY() + 0.05D, (double) position.getZ() + 0.5D, 0.0F, 0.0F);
        if (entity instanceof Mob mobEntity) {
            addFollowerGoals(mobEntity);
        }
        master.level.addFreshEntity(entity);
    }

    public static void addSummonGoals(Mob mobEntity) {
        Follower followerCap = getFollowerCapability(mobEntity);
        if (followerCap.isSummon()) {
            SummonConfig config = SummonConfigRegistry.getConfig(ForgeRegistries.ENTITY_TYPES.getKey(mobEntity.getType()));
            if (config.shouldAddAttackGoal()) {
                addSummonAttackGoal(mobEntity);
            }
        }
    }

    private static void addSummonAttackGoal(Mob mobEntity) {
        AttributeInstance attribute = mobEntity.getAttribute(ATTACK_DAMAGE);
        if (attribute == null) return;
        if (attribute.getValue() == 0) {
            attribute.addTransientModifier(new AttributeModifier("Summon Attack Damage", 1, AttributeModifier.Operation.ADDITION));
        }
        mobEntity.goalSelector.addGoal(1, new MeleeAttackGoal(mobEntity, 1.0D, true));
    }

}

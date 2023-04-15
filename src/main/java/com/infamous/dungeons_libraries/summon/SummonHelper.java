package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Follower;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Master;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Minion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
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
import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.getFollowerCapability;
import static net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE;

public class SummonHelper {

    private static boolean addSummonedMob(LivingEntity master, Entity entity) {
        Master masterCap = MinionMasterHelper.getMasterCapability(master);
        if (canSummonMob(master, entity, masterCap)) {
            return masterCap.addSummonedMob(entity);
        }
        return false;
    }

    private static boolean canSummonMob(LivingEntity master, Entity beeEntity, Master masterCap) {
        AttributeInstance summonCapAttribute = master.getAttribute(SUMMON_CAP.get());
        if (summonCapAttribute == null) return false;
        return masterCap.getSummonedMobsCost() + SummonConfigRegistry.getConfig(ForgeRegistries.ENTITY_TYPES.getKey(beeEntity.getType())).getCost() <= summonCapAttribute.getValue();
    }

    public static boolean canSummonMob(LivingEntity master, Master masterCap) {
        AttributeInstance summonCostLimitAttribute = master.getAttribute(SUMMON_CAP.get());
        if (summonCostLimitAttribute == null) return false;
        return masterCap.getSummonedMobsCost() < summonCostLimitAttribute.getValue();
    }

    public static Entity summonEntity(LivingEntity master, BlockPos position, EntityType<?> entityType) {
        Entity entity = entityType.create(master.level);
        if (entity != null) {
            Minion summonable = MinionMasterHelper.getMinionCapability(entity);
            if (addSummonedMob(master, entity)) {
                summonable.setMaster(master);
                summonable.setSummon(true);
                createSummon(master, entity, position);
                return entity;
            } else {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
        return null;
    }

    private static void createSummon(LivingEntity master, Entity entity, BlockPos position) {
        entity.moveTo((double) position.getX() + 0.5D, (double) position.getY() + 0.05D, (double) position.getZ() + 0.5D, 0.0F, 0.0F);
        if (entity instanceof Mob) {
            Mob mobEntity = (Mob) entity;
            MinionMasterHelper.addMinionGoals(mobEntity);
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

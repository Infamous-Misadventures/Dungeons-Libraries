package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.util.math.BlockPos;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SUMMON_CAP;

public class SummonHelper {

    private static boolean addSummonedMob(LivingEntity master, Entity entity) {
        IMaster masterCap = MinionMasterHelper.getMasterCapability(master);
        if(masterCap == null){
            return false;
        }
        if(canSummonMob(master, entity, masterCap)) {
            return masterCap.addSummonedMob(entity);
        }
        return false;
    }

    private static boolean canSummonMob(LivingEntity master, Entity beeEntity, IMaster masterCap) {
        ModifiableAttributeInstance summonCap = master.getAttribute(SUMMON_CAP.get());
        if(summonCap == null) return false;
        return masterCap.getSummonedMobsCost() + SummonConfigRegistry.getConfig(beeEntity.getType().getRegistryName()).getCost() <= summonCap.getValue();
    }

    public static boolean canSummonMob(LivingEntity master, IMaster masterCap) {
        ModifiableAttributeInstance summonCap = master.getAttribute(SUMMON_CAP.get());
        if(summonCap == null) return false;
        return masterCap.getSummonedMobsCost() < summonCap.getValue();
    }

    public static Entity summonEntity(LivingEntity master, BlockPos position, EntityType<?> entityType) {
        Entity entity = entityType.create(master.level);
        if(entity != null){
            IMinion summonable = MinionMasterHelper.getMinionCapability(entity);
            if(summonable != null && addSummonedMob(master, entity)){
                summonable.setMaster(master);
                summonable.setSummon(true);
                createSummon(master, entity, position);
                return entity;
            } else {
                entity.remove();
            }
        }
        return null;
    }

    private static void createSummon(LivingEntity master, Entity entity, BlockPos position) {
        entity.moveTo((double) position.getX() + 0.5D, (double) position.getY() + 0.05D, (double) position.getZ() + 0.5D, 0.0F, 0.0F);
        if(entity instanceof MobEntity) {
            MobEntity mobEntity = (MobEntity) entity;
            MinionMasterHelper.addMinionGoals(mobEntity);
        }
        master.level.addFreshEntity(entity);
    }

}

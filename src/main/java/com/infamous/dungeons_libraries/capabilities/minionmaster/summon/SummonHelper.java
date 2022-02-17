package com.infamous.dungeons_libraries.capabilities.minionmaster.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import com.infamous.dungeons_libraries.utils.SoundHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SUMMON_CAP;

public class SummonHelper {

    private static boolean addSummonedMob(LivingEntity master, LivingEntity beeEntity) {
        IMaster masterCap = MinionMasterHelper.getMasterCapability(master);
        if(masterCap == null){
            return false;
        }
        if( masterCap.getSummonedMobs().size() < master.getAttribute(SUMMON_CAP.get()).getValue()) {
            return masterCap.addSummonedMob(beeEntity.getUUID());
        }
        return false;
    }

    public static void summonBee(LivingEntity master, BlockPos position) {
        BeeEntity beeEntity = EntityType.BEE.create(master.level);
        if (beeEntity!= null) {
            IMinion summonable = MinionMasterHelper.getMinionCapability(beeEntity);
            if(summonable != null && addSummonedMob(master, beeEntity)){
                summonable.setMaster(master.getUUID());
                createBee(master, beeEntity, position);
            } else {
                beeEntity.remove();
            }
        }
    }

    public static void createBee(LivingEntity master, BeeEntity beeEntity, BlockPos position) {
        beeEntity.moveTo((double) position.getX() + 0.5D, (double) position.getY() + 0.05D, (double) position.getZ() + 0.5D, 0.0F, 0.0F);
        MinionMasterHelper.addMinionGoals(beeEntity);
        beeEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(beeEntity, LivingEntity.class, 5, false, false,
                (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));

        SoundHelper.playCreatureSound(master, SoundEvents.BEE_LOOP);
        master.level.addFreshEntity(beeEntity);
    }

}

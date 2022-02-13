package com.infamous.dungeons_libraries.capabilities.minionmaster.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import com.infamous.dungeons_libraries.utils.SoundHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SUMMON_CAP;

public class SummonHelper {

    private static boolean addSummonedMob(PlayerEntity playerEntity, MobEntity beeEntity) {
        IMaster masterCap = MinionMasterHelper.getMasterCapability(playerEntity);
        if(masterCap == null){
            return false;
        }
        if(playerEntity.getAttribute(SUMMON_CAP.get()).getValue() < masterCap.getSummonedMobs().size()) {
            return masterCap.addSummonedMob(beeEntity.getUUID());
        }
        return false;
    }

    public static void summonBee(PlayerEntity playerEntity, BlockPos position) {
        BeeEntity beeEntity = EntityType.BEE.create(playerEntity.level);
        if (beeEntity!= null) {
            IMinion summonable = MinionMasterHelper.getMinionCapability(beeEntity);
            if(summonable != null && addSummonedMob(playerEntity, beeEntity)){
                summonable.setMaster(playerEntity.getUUID());
                createBee(playerEntity, beeEntity, position);
            } else {
                beeEntity.remove();
            }
        }
    }

    public static void createBee(PlayerEntity playerEntity, BeeEntity beeEntity, BlockPos position) {
        beeEntity.moveTo((double) position.getX() + 0.5D, (double) position.getY() + 0.05D, (double) position.getZ() + 0.5D, 0.0F, 0.0F);
        MinionMasterHelper.addMinionGoals(beeEntity);
        beeEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(beeEntity, LivingEntity.class, 5, false, false,
                (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));

        SoundHelper.playCreatureSound(playerEntity, SoundEvents.BEE_LOOP);
        playerEntity.level.addFreshEntity(beeEntity);
    }

}

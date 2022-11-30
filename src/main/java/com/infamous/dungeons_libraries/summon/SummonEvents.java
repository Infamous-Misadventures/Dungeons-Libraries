package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = MODID)
public class SummonEvents {

    @SubscribeEvent
    public static void onSummonedMobAttemptsToAttack(LivingSetAttackTargetEvent event){
        if(event.getTarget() == null) return;
        if(MinionMasterHelper.isMinionEntity(event.getEntityLiving())){
            LivingEntity minionAttacker = event.getEntityLiving();
            IMinion attackerMinionCap = getMinionCapability(minionAttacker);
            if(attackerMinionCap == null) return;
            if(attackerMinionCap.getMaster() != null){
                LivingEntity attackersOwner = attackerMinionCap.getMaster();
                if(MinionMasterHelper.isMinionEntity(event.getTarget())){
                    LivingEntity summonableTarget = event.getTarget();
                    IMinion targetSummonableCap = getMinionCapability(summonableTarget);
                    if(targetSummonableCap == null) return;
                    if(targetSummonableCap.getMaster() != null){
                        LivingEntity targetsOwner = targetSummonableCap.getMaster();
                        if(targetsOwner.equals(attackersOwner)){
                            preventAttackForSummonableMob(minionAttacker);
                        }
                    }
                }
            }
            if(attackerMinionCap.getMaster() == event.getTarget()){
                preventAttackForSummonableMob(minionAttacker);
            }
        }
    }

    private static void preventAttackForSummonableMob(LivingEntity minionAttacker) {
        if(minionAttacker instanceof IAngerable){
            ((IAngerable) minionAttacker).stopBeingAngry();
        }
        if(minionAttacker instanceof MobEntity){
            MobEntity mob = (MobEntity) minionAttacker;
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
        }
    }
}

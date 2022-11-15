package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Minion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = MODID)
public class SummonEvents {

    @SubscribeEvent
    public static void reAddSummonableGoals(EntityJoinWorldEvent event){
        if(MinionMasterHelper.isMinionEntity(event.getEntity())){
            Minion minionCapability = getMinionCapability(event.getEntity());
            if(event.getEntity() instanceof Bee beeEntity){
                if(minionCapability.getMaster() != null){
                    beeEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(beeEntity, LivingEntity.class, 5, false, false,
                            (entityIterator) -> entityIterator instanceof Mob && !(entityIterator instanceof Creeper)));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onSummonedMobAttemptsToAttack(LivingSetAttackTargetEvent event){
        if(event.getTarget() == null) return;
        if(MinionMasterHelper.isMinionEntity(event.getEntityLiving())){
            LivingEntity minionAttacker = event.getEntityLiving();
            Minion attackerMinionCap = getMinionCapability(minionAttacker);
            if(attackerMinionCap.getMaster() != null){
                LivingEntity attackersOwner = attackerMinionCap.getMaster();
                if(MinionMasterHelper.isMinionEntity(event.getTarget())){
                    LivingEntity summonableTarget = event.getTarget();
                    Minion targetSummonableCap = getMinionCapability(summonableTarget);
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
        if(minionAttacker instanceof NeutralMob){
            ((NeutralMob) minionAttacker).stopBeingAngry();
        }
        if(minionAttacker instanceof Mob){
            Mob mob = (Mob) minionAttacker;
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
        }
    }
}

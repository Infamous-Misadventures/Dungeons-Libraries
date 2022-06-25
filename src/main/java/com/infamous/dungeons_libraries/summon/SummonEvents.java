package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import com.infamous.dungeons_libraries.capabilities.soulcaster.ISoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMasterCapability;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = MODID)
public class SummonEvents {

    // Avoids a situation where your summoned mob died but onSummonableDeath didn't fire in time,
    // making you unable to summon any more of that entity
    @SubscribeEvent
    public static void checkSummonedMobIsDead(TickEvent.PlayerTickEvent event){
        PlayerEntity summoner = event.player;
        if(event.phase == TickEvent.Phase.START || event.side == LogicalSide.CLIENT) return;
        if(!summoner.isAlive()) return;
        IMaster masterCap = getMasterCapability(event.player);
        if(masterCap == null) return;

        List<Entity> aliveMobs = masterCap.getSummonedMobs().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        masterCap.setSummonedMobs(aliveMobs);
    }

    @SubscribeEvent
    public static void onSummonableDeath(LivingDeathEvent event){
        if(!event.getEntityLiving().level.isClientSide() && MinionMasterHelper.isMinionEntity(event.getEntityLiving())){
            LivingEntity livingEntity = event.getEntityLiving();
            IMinion summonableCap = getMinionCapability(livingEntity);
            if(summonableCap == null) return;
            LivingEntity summoner = summonableCap.getMaster();
            if(summoner != null){
                IMaster masterCap = getMasterCapability(summoner);
                if(masterCap == null) return;
                List<Entity> aliveMobs = masterCap.getSummonedMobs().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
                masterCap.setSummonedMobs(aliveMobs);
            }
        }
    }

    @SubscribeEvent
    public static void reAddSummonableGoals(EntityJoinWorldEvent event){
        if(MinionMasterHelper.isMinionEntity(event.getEntity())){
            IMinion summonableCap = getMinionCapability(event.getEntity());
            if(summonableCap == null) return;
            if(event.getEntity() instanceof BeeEntity){
                BeeEntity beeEntity = (BeeEntity) event.getEntity();
                if(summonableCap.getMaster() != null){
                    beeEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(beeEntity, LivingEntity.class, 5, false, false,
                            (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));

                }
            }
        }
    }

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

package com.infamous.dungeons_libraries.capabilities.minionmaster.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.IMaster;
import com.infamous.dungeons_libraries.capabilities.minionmaster.IMinion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

        if(event.phase == TickEvent.Phase.START) return;
        if(!summoner.isAlive()) return;

        IMaster masterCap = getMasterCapability(event.player);
        if(masterCap == null) return;
        ServerWorld level = (ServerWorld) event.player.level;
        if(masterCap.getSummonedGolem() != null && event.player.level instanceof ServerWorld){
            UUID summonedGolem = masterCap.getSummonedGolem();
            Entity entity = level.getEntity(summonedGolem);
            if(!(entity instanceof IronGolemEntity)) {
                masterCap.setSummonedGolem(null);
//                ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.GOLEM_KIT.get());
            }
        }
        if(masterCap.getSummonedWolf() != null && event.player.level instanceof ServerWorld){
            UUID summonedWolf = masterCap.getSummonedWolf();
            Entity entity = level.getEntity(summonedWolf);
            if(!(entity instanceof WolfEntity)) {
                masterCap.setSummonedWolf(null);
//                ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.TASTY_BONE.get());
            }
        }
        if(masterCap.getSummonedLlama() != null && event.player.level instanceof ServerWorld){
            UUID summonedLlama = masterCap.getSummonedLlama();
            Entity entity = level.getEntity(summonedLlama);
            if(!(entity instanceof LlamaEntity)) {
                masterCap.setSummonedLlama(null);
//                ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.WONDERFUL_WHEAT.get());
            }
        }
        if(masterCap.getSummonedBat() != null && event.player.level instanceof ServerWorld){
            UUID summonedBat = masterCap.getSummonedBat();
            Entity entity = level.getEntity(summonedBat);
            if(!(entity instanceof BatEntity)) {
                masterCap.setSummonedBat(null);
            }
        }
        if(masterCap.getSummonedSheep() != null && event.player.level instanceof ServerWorld){
            UUID summonedSheep = masterCap.getSummonedSheep();
            Entity entity = level.getEntity(summonedSheep);
            if(!(entity instanceof SheepEntity)) {
                masterCap.setSummonedSheep(null);
//                ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.ENCHANTED_GRASS.get());
            }
        }
        List<UUID> summonedMobs = masterCap.getSummonedMobs();
        List<Entity> aliveMobs = summonedMobs.stream().map(level::getEntity).filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        masterCap.setSummonedMobs(aliveMobs.stream().map(Entity::getUUID).collect(Collectors.toList()));
    }

    @SubscribeEvent
    public static void onSummonableDeath(LivingDeathEvent event){
        if(!event.getEntityLiving().level.isClientSide() && MinionMasterHelper.isMinionEntity(event.getEntityLiving())){
            LivingEntity livingEntity = event.getEntityLiving();
            ServerWorld level = (ServerWorld) livingEntity.level;
            IMinion summonableCap = getMinionCapability(livingEntity);
            if(summonableCap == null) return;
            if(summonableCap.getMaster() != null){
                PlayerEntity summoner = level.getPlayerByUUID(summonableCap.getMaster());
                if(summoner != null){
                    IMaster masterCap = getMasterCapability(summoner);
                    if(masterCap == null) return;
                    UUID summonableUUID = livingEntity.getUUID();

                    if(masterCap.getSummonedGolem() == summonableUUID){
                        masterCap.setSummonedGolem(null);
//                        ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.GOLEM_KIT.get());
                    }
                    if(masterCap.getSummonedWolf() == summonableUUID){
                        masterCap.setSummonedWolf(null);
//                        ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.TASTY_BONE.get());
                    }
                    if(masterCap.getSummonedLlama() == summonableUUID){
                        masterCap.setSummonedLlama(null);
//                        ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.WONDERFUL_WHEAT.get());
                    }
                    if(masterCap.getSummonedBat() == summonableUUID){
                        masterCap.setSummonedBat(null);
                    }
                    if(masterCap.getSummonedSheep() == summonableUUID){
//                        ArtifactItem.putArtifactOnCooldown(summoner, ItemRegistry.ENCHANTED_GRASS.get());
                        masterCap.setSummonedSheep(null);
                    }
                    List<UUID> summonedMobs = masterCap.getSummonedMobs();
                    List<Entity> aliveMobs = summonedMobs.stream().map(level::getEntity).filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
                    masterCap.setSummonedMobs(aliveMobs.stream().map(Entity::getUUID).collect(Collectors.toList()));
                }
            }
        }
    }

    // Preserves ownership of summoned mobs on respawn
    // Prevents problems like summoning an entirely new summon of the same type
    // as one you already have after you respawn
    @SubscribeEvent
    public static void cloneSummonerCaps(PlayerEvent.Clone event){
        IMaster oldSummonerCap = getMasterCapability(event.getOriginal());
        IMaster newSummonerCap = getMasterCapability(event.getPlayer());
        if (oldSummonerCap != null && newSummonerCap != null) {
            newSummonerCap.copyFrom(oldSummonerCap);
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
//            if(event.getEntity() instanceof LlamaEntity){
//                LlamaEntity llamaEntity = (LlamaEntity) event.getEntity();
//                if(summonableCap.getMaster() != null){
//                    llamaEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(llamaEntity, LivingEntity.class, 5, false, false,
//                            (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));
//                }
//            }
//            if(event.getEntity() instanceof WolfEntity){
//                WolfEntity wolfEntity = (WolfEntity) event.getEntity();
//                if(summonableCap.getMaster() != null){
//                    wolfEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(wolfEntity, LivingEntity.class, 5, false, false,
//                            (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));
//                }
//            }
//            if(event.getEntity() instanceof BatEntity){
//                BatEntity batEntity = (BatEntity) event.getEntity();
//                if(summonableCap.getMaster() != null){
//                    batEntity.goalSelector.addGoal(1, new BatMeleeAttackGoal(batEntity, 1.0D, true));
//                    batEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(batEntity, LivingEntity.class, 5, false, false,
//                            (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));
//                }
//            }
//            if(event.getEntity() instanceof SheepEntity){
//                SheepEntity sheepEntity = (SheepEntity) event.getEntity();
//                if(summonableCap.getMaster() != null){
//                    if(sheepEntity.getTags().contains(FIRE_SHEEP_TAG) || sheepEntity.getTags().contains(POISON_SHEEP_TAG)){
//                        sheepEntity.goalSelector.addGoal(1, new SheepMeleeAttackGoal(sheepEntity, 1.0D, true));
//
//                        sheepEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(sheepEntity, LivingEntity.class, 5, false, false,
//                                (entityIterator) -> entityIterator instanceof IMob && !(entityIterator instanceof CreeperEntity)));
//                    }
//                }
//            }
        }
    }
}

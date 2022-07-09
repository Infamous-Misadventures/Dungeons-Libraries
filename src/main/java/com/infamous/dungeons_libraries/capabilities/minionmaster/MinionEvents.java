package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMasterCapability;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class MinionEvents {

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event){
        IMinion cap = MinionMasterHelper.getMinionCapability(event.getEntityLiving());
        if (cap != null && cap.isMinion()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingEntityTick(LivingEvent.LivingUpdateEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if(entityLiving.level.isClientSide) return;
        IMinion cap = MinionMasterHelper.getMinionCapability(entityLiving);
        if (cap != null && cap.isMinion()) {
            if (cap.isTemporary()) {
                if (cap.getMinionTimer() > 0) {
                    cap.setMinionTimer(cap.getMinionTimer() - 1);
                } else {
                    if (cap.revertsOnExpiration()) {
                        MinionMasterHelper.removeMinion(entityLiving, cap);
                    } else {
                        entityLiving.remove();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void reAddMinionGoals(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(!event.getWorld().isClientSide() && entity instanceof MobEntity) {
            MinionMasterHelper.addMinionGoals((MobEntity) entity);
        }
    }

    // Avoids a situation where your minion died but onSummonableDeath didn't fire in time,
    // making you unable to summon any more of that entity
    @SubscribeEvent
    public static void checkSummonedMobIsDead(TickEvent.PlayerTickEvent event){
        PlayerEntity master = event.player;
        if(event.phase == TickEvent.Phase.START || event.side == LogicalSide.CLIENT) return;
        if(!master.isAlive()) return;
        IMaster masterCap = getMasterCapability(event.player);
        if(masterCap == null) return;
        updateAliveList(masterCap);
    }

    @SubscribeEvent
    public static void onMinionDeath(LivingDeathEvent event){
        if(!event.getEntityLiving().level.isClientSide() && MinionMasterHelper.isMinionEntity(event.getEntityLiving())){
            LivingEntity livingEntity = event.getEntityLiving();
            IMinion minionCapability = getMinionCapability(livingEntity);
            if(minionCapability == null) return;
            LivingEntity summoner = minionCapability.getMaster();
            if(summoner != null){
                IMaster masterCap = getMasterCapability(summoner);
                if(masterCap == null) return;
                updateAliveList(masterCap);
            }
        }
    }

    private static void updateAliveList(IMaster masterCap) {
        List<Entity> aliveSummons = masterCap.getSummonedMobs().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        masterCap.setSummonedMobs(aliveSummons);
        List<Entity> aliveMinions = masterCap.getOtherMinions().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        masterCap.setOtherMinions(aliveMinions);
    }

}

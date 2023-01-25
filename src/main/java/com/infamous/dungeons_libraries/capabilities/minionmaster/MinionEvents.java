package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.utils.AbilityHelper;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
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

    private static ArmorStand DUMMY_TARGET;

    @SubscribeEvent
    public static void onSetAttackTarget(LivingChangeTargetEvent event) {
        LivingEntity attacker = event.getEntity();
        Level level = attacker.level;
        LivingEntity target = event.getNewTarget();
        if (attacker instanceof Mob && target instanceof Mob) {
            if (AbilityHelper.isAlly(attacker, target)) {
                createDummyTarget(level);
                if (attacker instanceof NeutralMob) {
                    ((NeutralMob) attacker).setPersistentAngerTarget(null);
                    ((NeutralMob) attacker).setRemainingPersistentAngerTime(0);
                }
                ((Mob) attacker).setTarget(DUMMY_TARGET);
                attacker.setLastHurtByMob(DUMMY_TARGET);
            }
        }
    }

    private static void createDummyTarget(Level level) {
        if (DUMMY_TARGET == null) {
            DUMMY_TARGET = EntityType.ARMOR_STAND.create(level);
            if (DUMMY_TARGET != null) {
                DUMMY_TARGET.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event) {
        Minion cap = MinionMasterHelper.getMinionCapability(event.getEntity());
        if (cap.isMinion()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving.level.isClientSide) return;
        Minion cap = MinionMasterHelper.getMinionCapability(entityLiving);
        if (cap.isMinion()) {
            if (cap.isTemporary()) {
                if (cap.getMinionTimer() > 0) {
                    cap.setMinionTimer(cap.getMinionTimer() - 1);
                } else {
                    if (cap.revertsOnExpiration()) {
                        MinionMasterHelper.removeMinion(entityLiving, cap);
                    } else {
                        entityLiving.remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void reAddMinionGoals(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!event.getLevel().isClientSide() && entity instanceof Mob) {
            MinionMasterHelper.addMinionGoals((Mob) entity);
            Master masterCapability = getMasterCapability(entity);
            List<Entity> minions = masterCapability.getAllMinions();
            for (Entity minion : minions) {
                if (minion instanceof Mob) {
                    Minion minionCapability = getMinionCapability(minion);
                    minionCapability.setMaster((Mob) entity);
                    MinionMasterHelper.addMinionGoals((Mob) minion);
                }
            }
        }
        if (!event.getLevel().isClientSide() && entity instanceof Player) {
            Master masterCapability = getMasterCapability(entity);
            List<Entity> minions = masterCapability.getAllMinions();
            for (Entity minion : minions) {
                if (minion instanceof Mob) {
                    Minion minionCapability = getMinionCapability(minion);
                    minionCapability.setMaster((Player) entity);
                    MinionMasterHelper.addMinionGoals((Mob) minion);
                }
            }
        }
    }

    // Avoids a situation where your minion died but onSummonableDeath didn't fire in time,
    // making you unable to summon any more of that entity
    @SubscribeEvent
    public static void checkSummonedMobIsDead(TickEvent.PlayerTickEvent event) {
        Player master = event.player;
        if (event.phase == TickEvent.Phase.START || event.side == LogicalSide.CLIENT) return;
        if (!master.isAlive()) return;
        Master masterCap = getMasterCapability(event.player);
        updateAliveList(masterCap);
    }

    @SubscribeEvent
    public static void onMinionDeath(LivingDeathEvent event) {
        if (!event.getEntity().level.isClientSide() && MinionMasterHelper.isMinionEntity(event.getEntity())) {
            LivingEntity livingEntity = event.getEntity();
            Minion minionCapability = getMinionCapability(livingEntity);
            LivingEntity summoner = minionCapability.getMaster();
            if (summoner != null) {
                Master masterCap = getMasterCapability(summoner);
                updateAliveList(masterCap);
            }
        }
    }

    private static void updateAliveList(Master masterCap) {
        List<Entity> aliveSummons = masterCap.getSummonedMobs().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        masterCap.setSummonedMobs(aliveSummons);
        List<Entity> aliveMinions = masterCap.getOtherMinions().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        masterCap.setOtherMinions(aliveMinions);
    }

}

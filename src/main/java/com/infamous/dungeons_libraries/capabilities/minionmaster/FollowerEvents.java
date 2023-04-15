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

import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.getFollowerCapability;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.getLeaderCapability;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class FollowerEvents {

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
        Follower cap = getFollowerCapability(event.getEntity());
        if (cap.isFollower()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingEntityTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving.level.isClientSide) return;
        Follower cap = getFollowerCapability(entityLiving);
        if (cap.isFollower()) {
            if (cap.isTemporary()) {
                if (cap.getFollowerDuration() > 0) {
                    cap.setFollowerDuration(cap.getFollowerDuration() - 1);
                } else {
                    if (cap.revertsOnExpiration()) {
                        FollowerLeaderHelper.removeFollower(entityLiving);
                    } else {
                        entityLiving.remove(Entity.RemovalReason.KILLED);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void reAddFollowerGoals(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!event.getLevel().isClientSide() && entity instanceof LivingEntity livingEntity) {
            Leader leaderCapability = getLeaderCapability(entity);
            if(entity instanceof Mob mob) {
                FollowerLeaderHelper.addFollowerGoals(mob);
            }
            List<Entity> minions = leaderCapability.getAllMinions();
            for (Entity minion : minions) {
                if (minion instanceof Mob) {
                    Follower minionCapability = getFollowerCapability(minion);
                    minionCapability.setLeader(livingEntity);
                    FollowerLeaderHelper.addFollowerGoals((Mob) minion);
                }
            }
        }
    }

    // Avoids a situation where your minion died but onSummonableDeath didn't fire in time,
    // making you unable to summon any more of that entity
    @SubscribeEvent
    public static void checkSummonedMobIsDead(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.START || event.side == LogicalSide.CLIENT) return;
        if (!player.isAlive()) return;
        Leader leaderCap = getLeaderCapability(event.player);
        updateAliveList(leaderCap);
    }

    @SubscribeEvent
    public static void onFollowerDeath(LivingDeathEvent event) {
        if (!event.getEntity().level.isClientSide() && FollowerLeaderHelper.isFollower(event.getEntity())) {
            LivingEntity livingEntity = event.getEntity();
            Follower FollowerCapability = getFollowerCapability(livingEntity);
            LivingEntity summoner = FollowerCapability.getLeader();
            if (summoner != null) {
                Leader leaderCapability = getLeaderCapability(summoner);
                updateAliveList(leaderCapability);
            }
        }
    }

    private static void updateAliveList(Leader leader) {
        List<Entity> aliveSummons = leader.getSummonedMobs().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        leader.setSummonedMobs(aliveSummons);
        List<Entity> aliveMinions = leader.getOtherMinions().stream().filter(entity -> entity != null && entity.isAlive()).collect(Collectors.toList());
        leader.setOtherMinions(aliveMinions);
    }

}

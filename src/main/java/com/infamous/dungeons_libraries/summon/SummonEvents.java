package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Follower;
import com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.getFollowerCapability;

@Mod.EventBusSubscriber(modid = MODID)
public class SummonEvents {

    @SubscribeEvent
    public static void onSummonedMobAttemptsToAttack(LivingChangeTargetEvent event) {
        if (event.getNewTarget() == null) return;
        if (FollowerLeaderHelper.isFollower(event.getEntity())) {
            LivingEntity followerAttacker = event.getEntity();
            Follower attackerFollowerCapability = getFollowerCapability(followerAttacker);
            if (attackerFollowerCapability.getMaster() != null) {
                LivingEntity attackersOwner = attackerFollowerCapability.getMaster();
                if (FollowerLeaderHelper.isFollower(event.getNewTarget())) {
                    LivingEntity summonableTarget = event.getNewTarget();
                    Follower targetFollowerCapability = getFollowerCapability(summonableTarget);
                    if (targetFollowerCapability.getLeader() != null) {
                        LivingEntity targetsOwner = targetFollowerCapability.getMaster();
                        if (targetsOwner.equals(attackersOwner)) {
                            event.setCanceled(true);
                            preventAttackForSummonableMob(followerAttacker);
                        }
                    }
                }
            }
            if (attackerFollowerCapability.getMaster() == event.getNewTarget()) {
                event.setCanceled(true);
                preventAttackForSummonableMob(followerAttacker);
            }
        }
    }

    private static void preventAttackForSummonableMob(LivingEntity followerAttacker) {
        if (followerAttacker instanceof NeutralMob) {
            ((NeutralMob) followerAttacker).stopBeingAngry();
        }
    }
}

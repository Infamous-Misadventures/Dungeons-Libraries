package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Minion;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = MODID)
public class SummonEvents {

    @SubscribeEvent
    public static void onSummonedMobAttemptsToAttack(LivingChangeTargetEvent event) {
        if (event.getNewTarget() == null) return;
        if (MinionMasterHelper.isMinionEntity(event.getEntity())) {
            LivingEntity minionAttacker = event.getEntity();
            Minion attackerMinionCap = getMinionCapability(minionAttacker);
            if (attackerMinionCap.getMaster() != null) {
                LivingEntity attackersOwner = attackerMinionCap.getMaster();
                if (MinionMasterHelper.isMinionEntity(event.getNewTarget())) {
                    LivingEntity summonableTarget = event.getNewTarget();
                    Minion targetSummonableCap = getMinionCapability(summonableTarget);
                    if (targetSummonableCap.getMaster() != null) {
                        LivingEntity targetsOwner = targetSummonableCap.getMaster();
                        if (targetsOwner.equals(attackersOwner)) {
                            event.setCanceled(true);
                            preventAttackForSummonableMob(minionAttacker);
                        }
                    }
                }
            }
            if (attackerMinionCap.getMaster() == event.getNewTarget()) {
                event.setCanceled(true);
                preventAttackForSummonableMob(minionAttacker);
            }
        }
    }

    private static void preventAttackForSummonableMob(LivingEntity minionAttacker) {
        if (minionAttacker instanceof NeutralMob) {
            ((NeutralMob) minionAttacker).stopBeingAngry();
        }
    }
}

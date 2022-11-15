package com.infamous.dungeons_libraries.capabilities.minionmaster.goals;

import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

import static com.infamous.dungeons_libraries.utils.AbilityHelper.isAlly;
import static com.infamous.dungeons_libraries.utils.GoalUtils.shouldAttackEntity;

public class MasterHurtTargetGoal extends TargetGoal {
    public static final EntityPredicate PREDICATE = new EntityPredicate().allowSameTeam();
    private final MobEntity mobEntity;
    private LivingEntity attacker;
    private int timestamp;

    public MasterHurtTargetGoal(MobEntity mobEntity) {
        super(mobEntity, false);
        this.mobEntity = mobEntity;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
            LivingEntity owner = MinionMasterHelper.getMaster(this.mobEntity);
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner.getLastHurtMob();
                int lastAttackedEntityTime = owner.getLastHurtMobTimestamp();
                return lastAttackedEntityTime != this.timestamp && this.canAttack(this.attacker, PREDICATE) && !isAlly(this.mobEntity, this. attacker) && shouldAttackEntity(this.attacker, owner);
            }
//        } else {
//            return false;
//        }
    }

    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity owner = MinionMasterHelper.getMaster(this.mobEntity);
        if (owner != null) {
            this.timestamp = owner.getLastHurtMobTimestamp();
        }

        super.start();
    }
}

package com.infamous.dungeons_libraries.capabilities.minionmaster.goals;

import com.infamous.dungeons_libraries.entities.ai.target.MinionTargettingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.getLeader;
import static com.infamous.dungeons_libraries.utils.GoalUtils.shouldAttackEntity;

public class LeaderHurtByTargetGoal extends TargetGoal {
    MinionTargettingConditions PREDICATE = new MinionTargettingConditions();
    private final Mob mobEntity;
    private LivingEntity attacker;
    private int timestamp;

    public LeaderHurtByTargetGoal(Mob mobEntity) {
        super(mobEntity, false);
        this.mobEntity = mobEntity;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
//        if (this.mobEntity.isPlayerCreated()) {
        LivingEntity owner = getLeader(this.mobEntity);
        if (owner == null) {
            return false;
        } else {
            this.attacker = owner.getLastHurtByMob();
            int revengeTimer = owner.getLastHurtByMobTimestamp();
            return revengeTimer != this.timestamp && this.canAttack(this.attacker, PREDICATE) && shouldAttackEntity(this.attacker, owner);
        }
//        } else {
//            return false;
//        }
    }

    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity owner = getLeader(this.mobEntity);
        if (owner != null) {
            this.timestamp = owner.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}

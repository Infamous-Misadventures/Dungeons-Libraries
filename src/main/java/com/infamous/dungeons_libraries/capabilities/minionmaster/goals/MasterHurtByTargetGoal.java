package com.infamous.dungeons_libraries.capabilities.minionmaster.goals;

import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.TargetGoal;

import java.util.EnumSet;

import static com.infamous.dungeons_libraries.utils.AbilityHelper.isAlly;
import static com.infamous.dungeons_libraries.utils.GoalUtils.shouldAttackEntity;

public class MasterHurtByTargetGoal extends TargetGoal {
    public static final EntityPredicate PREDICATE = new EntityPredicate().allowSameTeam();
    private final MobEntity mobEntity;
    private LivingEntity attacker;
    private int timestamp;

    public MasterHurtByTargetGoal(MobEntity mobEntity) {
        super(mobEntity, false);
        this.mobEntity = mobEntity;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
//        if (this.mobEntity.isPlayerCreated()) {
            LivingEntity owner = MinionMasterHelper.getMaster(this.mobEntity);
            if (owner == null) {
                return false;
            } else {
                this.attacker = owner.getLastHurtByMob();
                int revengeTimer = owner.getLastHurtByMobTimestamp();
                return revengeTimer != this.timestamp && this.canAttack(this.attacker, PREDICATE) && !isAlly(this.mobEntity, this. attacker) && shouldAttackEntity(this.attacker, owner);
            }
//        } else {
//            return false;
//        }
    }

    public void start() {
        this.mob.setTarget(this.attacker);
        LivingEntity owner = MinionMasterHelper.getMaster(this.mobEntity);
        if (owner != null) {
            this.timestamp = owner.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}

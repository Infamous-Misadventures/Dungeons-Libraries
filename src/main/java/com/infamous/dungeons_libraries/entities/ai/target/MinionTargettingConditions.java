package com.infamous.dungeons_libraries.entities.ai.target;

import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.utils.AbilityHelper.isAlly;

public class MinionTargettingConditions extends TargetingConditions {

    public MinionTargettingConditions() {
        super(false);
    }

    public boolean test(@Nullable LivingEntity attacker, LivingEntity target) {
        return super.test(attacker, target) && (target.canBeSeenAsEnemy() || target.level.getDifficulty() != Difficulty.PEACEFUL) && attacker.canAttack(target) && !isAlly(attacker, target);
    }
}

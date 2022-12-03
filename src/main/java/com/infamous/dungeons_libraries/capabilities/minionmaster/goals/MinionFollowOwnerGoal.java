package com.infamous.dungeons_libraries.capabilities.minionmaster.goals;

import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class MinionFollowOwnerGoal extends Goal {
    private final Mob mobEntity;
    private LivingEntity owner;
    private final LevelReader world;
    private final double followSpeed;
    private final PathNavigation navigator;
    private int timeToRecalcPath;
    private final float minDist;
    private final float maxDist;
    private float oldWaterCost;
    private final boolean passesThroughLeaves;

    public MinionFollowOwnerGoal(Mob mobEntity, double followSpeed, float maxDist, float minDist, boolean passesThroughLeaves) {
        this.mobEntity = mobEntity;
        this.world = mobEntity.level;
        this.followSpeed = followSpeed;
        this.navigator = mobEntity.getNavigation();
        this.maxDist = maxDist;
        this.minDist = minDist;
        this.passesThroughLeaves = passesThroughLeaves;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (!(mobEntity.getNavigation() instanceof GroundPathNavigation) && !(mobEntity.getNavigation() instanceof FlyingPathNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean canUse() {
        LivingEntity livingentity = MinionMasterHelper.getMaster(this.mobEntity);
        if (livingentity == null) {
            return false;
        } else if (livingentity.isSpectator()) {
            return false;
        } else if (this.mobEntity.isLeashed()) {
            return false;
        } else if (this.mobEntity.distanceTo(livingentity) < (double)(this.maxDist)) {
            return false;
        } else {
            this.owner = livingentity;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean canContinueToUse() {
        if (this.navigator.isDone()) {
            return false;
        } else if (this.mobEntity.isLeashed()) {
            return false;
        } else {
            return !(this.mobEntity.distanceTo(this.owner) <= (double)(this.minDist));
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mobEntity.getPathfindingMalus(BlockPathTypes.WATER);
        this.mobEntity.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void stop() {
        this.owner = null;
        this.navigator.stop();
        this.mobEntity.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.mobEntity.getLookControl().setLookAt(this.owner, 10.0F, (float)this.mobEntity.getMaxHeadXRot());
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.mobEntity.isLeashed() && !this.mobEntity.isPassenger()) {
                if (this.mobEntity.distanceTo(this.owner) >= maxDist*2) {
                    this.teleportToOwner();
                } else {
                    this.navigator.moveTo(this.owner, this.followSpeed);
                }

            }
        }
    }

    private void teleportToOwner() {
        BlockPos blockpos = this.owner.blockPosition();

        for(int i = 0; i < 10; ++i) {
            int j = this.randomIntInclusive(-3, 3);
            int k = this.randomIntInclusive(-1, 1);
            int l = this.randomIntInclusive(-3, 3);
            boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
            if (flag) {
                return;
            }
        }

    }

    private boolean maybeTeleportTo(int p_226328_1_, int p_226328_2_, int p_226328_3_) {
        if (Math.abs((double)p_226328_1_ - this.owner.getX()) < 2.0D && Math.abs((double)p_226328_3_ - this.owner.getZ()) < 2.0D) {
            return false;
        } else if (!this.canTeleportTo(new BlockPos(p_226328_1_, p_226328_2_, p_226328_3_))) {
            return false;
        } else {
            this.mobEntity.moveTo((double)p_226328_1_ + 0.5D, (double)p_226328_2_, (double)p_226328_3_ + 0.5D, this.mobEntity.getYRot(), this.mobEntity.getXRot());
            this.navigator.stop();
            return true;
        }
    }

    private boolean canTeleportTo(BlockPos p_226329_1_) {
        BlockPathTypes pathnodetype = WalkNodeEvaluator.getBlockPathTypeStatic(this.world, p_226329_1_.mutable());
        if (pathnodetype != BlockPathTypes.WALKABLE) {
            return false;
        } else {
            BlockState blockstate = this.world.getBlockState(p_226329_1_.below());
            if (!this.passesThroughLeaves && blockstate.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos blockpos = p_226329_1_.subtract(this.mobEntity.blockPosition());
                return this.world.noCollision(this.mobEntity, this.mobEntity.getBoundingBox().move(blockpos));
            }
        }
    }

    private int randomIntInclusive(int p_226327_1_, int p_226327_2_) {
        return this.mobEntity.getRandom().nextInt(p_226327_2_ - p_226327_1_ + 1) + p_226327_1_;
    }
}

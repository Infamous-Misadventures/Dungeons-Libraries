package com.infamous.dungeons_libraries.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class TotemBaseEntity extends Entity {
    private final TargetingConditions entityTargeting = TargetingConditions.forCombat().range(8.0D).ignoreLineOfSight().ignoreInvisibilityTesting();

    protected int lifeTicks = 80;
    protected int deathTicks = 40;
    private float totemDeathAnimationO;
    private float totemDeathAnimation;

    private LivingEntity owner;
    private UUID ownerUUID;

    public TotemBaseEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_) {
        super(p_i48580_1_, p_i48580_2_);
    }

    public TotemBaseEntity(EntityType<?> p_i48580_1_, Level p_i48580_2_, int lifeTicks, int deathTicks) {
        super(p_i48580_1_, p_i48580_2_);
        this.lifeTicks = lifeTicks;
        this.deathTicks = deathTicks;
    }

    @Override
    protected void defineSynchedData() {

    }

    public int getLifeTicks() {
        return lifeTicks;
    }

    public int getDeathTicks() {
        return deathTicks;
    }

    //set How long Totem is alive
    public void setLifeTicks(int lifeTicks) {
        this.lifeTicks = lifeTicks;
    }

    public boolean isTotemDeath() {
        return this.lifeTicks <= 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide) {

            this.totemDeathAnimationO = this.totemDeathAnimation;
            if (this.isTotemDeath()) {
                this.totemDeathAnimation = Mth.clamp(this.totemDeathAnimation + 0.1F, 0.0F, 1.0F);
            } else {
                this.totemDeathAnimation = Mth.clamp(this.totemDeathAnimation - 0.1F, 0.0F, 1.0F);
            }
        }


        if (this.lifeTicks > 0) {
            --this.lifeTicks;
            this.applyTotemEffect();
        } else {
            if (this.deathTicks > 0) {
                --this.deathTicks;
            } else {
                this.remove(RemovalReason.DISCARDED);
            }
        }
    }

    protected abstract void applyTotemEffect();

    @OnlyIn(Dist.CLIENT)
    public float getTotemDeathAnimationScale(float p_189795_1_) {
        return Mth.lerp(p_189795_1_, this.totemDeathAnimationO, this.totemDeathAnimation);
    }

    //set Using Owner
    public void setOwner(@Nullable LivingEntity p_190549_1_) {
        this.owner = p_190549_1_;
        this.ownerUUID = p_190549_1_ == null ? null : p_190549_1_.getUUID();
    }

    @Nullable
    public LivingEntity getOwner() {
        if (this.owner == null && this.ownerUUID != null && this.level instanceof ServerLevel) {
            Entity entity = ((ServerLevel) this.level).getEntity(this.ownerUUID);
            if (entity instanceof LivingEntity) {
                this.owner = (LivingEntity) entity;
            }
        }

        return this.owner;
    }


    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("LifeTicks", 99)) {
            this.lifeTicks = pCompound.getInt("LifeTicks");
        }
        if (pCompound.contains("DeathTicks", 99)) {
            this.deathTicks = pCompound.getInt("DeathTicks");
        }
        if (pCompound.hasUUID("Owner")) {
            this.ownerUUID = pCompound.getUUID("Owner");
        }

    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("LifeTicks", this.lifeTicks);
        pCompound.putInt("DeathTicks", this.deathTicks);
        if (this.ownerUUID != null) {
            pCompound.putUUID("Owner", this.ownerUUID);
        }

    }
}

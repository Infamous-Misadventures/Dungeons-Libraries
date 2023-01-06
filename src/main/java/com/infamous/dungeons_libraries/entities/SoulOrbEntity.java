package com.infamous.dungeons_libraries.entities;

import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.infamous.dungeons_libraries.event.PlayerSoulEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import java.util.UUID;

public class SoulOrbEntity extends Entity implements IEntityAdditionalSpawnData {
    public int tickCount;
    public int age;
    public int floatTime = 20;
    private int health = 5;
    public float value;
    private Player followingPlayer;
    private int followingTime;

    public SoulOrbEntity(Player followingPlayer, Level level, double x, double y, double z, float value) {
        this(ModEntityTypes.SOUL_ORB.get(), level);
        this.followingPlayer = followingPlayer;
        this.setPos(x, y, z);
        this.setYRot((float) (this.random.nextDouble() * 360.0D));
        this.setDeltaMovement((this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double) 0.2F - (double) 0.1F) * 2.0D);
        this.value = value;
        this.setNoGravity(true);
    }

    public SoulOrbEntity(EntityType<? extends SoulOrbEntity> p_i50382_1_, Level p_i50382_2_) {
        super(p_i50382_1_, p_i50382_2_);
    }

    protected boolean isMovementNoisy() {
        return false;
    }

    protected void defineSynchedData() {
    }

    public void tick() {
        super.tick();
        this.setDeltaMovement(0.0D, 0.0D, 0.0D);
        if (this.floatTime > 0) {
            this.setDeltaMovement(0.0D, 0.05D, 0.0D);
            --this.floatTime;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        if (this.isEyeInFluid(FluidTags.WATER)) {
            this.setUnderwaterMovement();
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
        }

        if (!this.level.noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
        }

        double d0 = 8.0D;
        if (this.followingPlayer == null || this.followingPlayer.isSpectator()) {
            this.discard();
        }

        if (this.followingPlayer != null && this.floatTime == 0) {
            Vec3 vector3d = new Vec3(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double) this.followingPlayer.getEyeHeight() / 2.0D - this.getY(), this.followingPlayer.getZ() - this.getZ());
            double d1 = this.tickCount * 0.0075;
           this.setDeltaMovement(this.getDeltaMovement().add(vector3d.normalize().scale(d1)));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        float f = 0.98F;
        this.setDeltaMovement(this.getDeltaMovement().multiply(f, f, f));

        ++this.tickCount;
        ++this.age;
        if (this.age >= 6000) {
            this.discard();
        }

    }

    private void setUnderwaterMovement() {
        Vec3 vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x * (double) 0.99F, Math.min(vector3d.y + (double) 5.0E-4F, 0.06F), vector3d.z * (double) 0.99F);
    }

    protected void doWaterSplashEffect() {
    }

    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if (this.level.isClientSide || this.isRemoved()) return false; //Forge: Fixes MC-53850
        if (this.isInvulnerableTo(p_70097_1_)) {
            return false;
        } else {
            this.markHurt();
            this.health = (int) ((float) this.health - p_70097_2_);
            if (this.health <= 0) {
                this.remove(RemovalReason.KILLED);
            }

            return false;
        }
    }

    public void addAdditionalSaveData(CompoundTag p_213281_1_) {
        p_213281_1_.putShort("Health", (short) this.health);
        p_213281_1_.putShort("Age", (short) this.age);
        p_213281_1_.putShort("Value", (short) this.value);
        p_213281_1_.putShort("FloatTime", (short) this.floatTime);
        p_213281_1_.putUUID("FollowingPlayer", this.followingPlayer.getUUID());
    }

    public void readAdditionalSaveData(CompoundTag p_70037_1_) {
        this.health = p_70037_1_.getShort("Health");
        this.age = p_70037_1_.getShort("Age");
        this.value = p_70037_1_.getShort("Value");
        this.floatTime = p_70037_1_.getShort("FloatTime");
        UUID followingPlayerUUID = p_70037_1_.getUUID("FollowingPlayer");
        Player playerByUUID = this.level.getPlayerByUUID(followingPlayerUUID);
        if (playerByUUID != null) {
            this.followingPlayer = playerByUUID;
        }
    }

    public void playerTouch(Player player) {
        if (!this.level.isClientSide) {
            if (this.floatTime == 0) {
                //Throw OrbEvent
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new PlayerSoulEvent.PickupSoul(player, this)))
                    return;

                if (this.value > 0) {
                    SoulCasterHelper.addSouls(player, this.value);
                }

                this.discard();
            }

        }
    }

    public float getValue() {
        return this.value;
    }

    public int getIcon() {
        if (this.value >= 10) {
            return 10;
        } else if (this.value <= 0) {
            return 0;
        } else {
            return (int) Math.floor((this.value - 1) / 3) + 7;
        }
    }

    public boolean isAttackable() {
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.health);
        buffer.writeInt(this.age);
        buffer.writeFloat(this.value);
        buffer.writeUUID(this.followingPlayer.getUUID());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.health = additionalData.readInt();
        this.age = additionalData.readInt();
        this.value = additionalData.readFloat();
        Player playerByUUID = this.level.getPlayerByUUID(additionalData.readUUID());
        if (playerByUUID != null) {
            this.followingPlayer = playerByUUID;
        }
    }
}

package com.infamous.dungeons_libraries.entities;

import com.infamous.dungeons_libraries.capabilities.soulcaster.ISoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class SoulOrbEntity extends Entity implements IEntityAdditionalSpawnData {
   public int tickCount;
   public int age;
   public int floatTime = 20;
   private int health = 5;
   public float value;
   private PlayerEntity followingPlayer;
   private int followingTime;

   public SoulOrbEntity(PlayerEntity followingPlayer, World level, double x, double y, double z, float value) {
      this(ModEntityTypes.SOUL_ORB.get(), level);
      this.followingPlayer = followingPlayer;
      this.setPos(x, y, z);
      this.yRot = (float)(this.random.nextDouble() * 360.0D);
      this.setDeltaMovement((this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * (double)0.2F - (double)0.1F) * 2.0D);
      this.value = value;
      this.setNoGravity(true);
   }

   public SoulOrbEntity(EntityType<? extends SoulOrbEntity> p_i50382_1_, World p_i50382_2_) {
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
         this.remove();
      }

      if (this.followingPlayer != null && this.floatTime == 0) {
         Vector3d vector3d = new Vector3d(this.followingPlayer.getX() - this.getX(), this.followingPlayer.getY() + (double)this.followingPlayer.getEyeHeight() / 2.0D - this.getY(), this.followingPlayer.getZ() - this.getZ());
         double d1 = this.tickCount * 0.0075; ;
         this.setDeltaMovement(this.getDeltaMovement().add(vector3d.normalize().scale(d1)));
      }

      this.move(MoverType.SELF, this.getDeltaMovement());
      float f = 0.98F;
      this.setDeltaMovement(this.getDeltaMovement().multiply(f, f, f));

      ++this.tickCount;
      ++this.age;
      if (this.age >= 6000) {
         this.remove();
      }

   }

   private void setUnderwaterMovement() {
      Vector3d vector3d = this.getDeltaMovement();
      this.setDeltaMovement(vector3d.x * (double)0.99F, Math.min(vector3d.y + (double)5.0E-4F, (double)0.06F), vector3d.z * (double)0.99F);
   }

   protected void doWaterSplashEffect() {
   }

   public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
      if (this.level.isClientSide || this.removed) return false; //Forge: Fixes MC-53850
      if (this.isInvulnerableTo(p_70097_1_)) {
         return false;
      } else {
         this.markHurt();
         this.health = (int)((float)this.health - p_70097_2_);
         if (this.health <= 0) {
            this.remove();
         }

         return false;
      }
   }

   public void addAdditionalSaveData(CompoundNBT p_213281_1_) {
      p_213281_1_.putShort("Health", (short)this.health);
      p_213281_1_.putShort("Age", (short)this.age);
      p_213281_1_.putShort("Value", (short)this.value);
      p_213281_1_.putShort("FloatTime", (short)this.floatTime);
      p_213281_1_.putUUID("FollowingPlayer", this.followingPlayer.getUUID());
   }

   public void readAdditionalSaveData(CompoundNBT p_70037_1_) {
      this.health = p_70037_1_.getShort("Health");
      this.age = p_70037_1_.getShort("Age");
      this.value = p_70037_1_.getShort("Value");
      this.floatTime = p_70037_1_.getShort("FloatTime");
      UUID followingPlayerUUID = p_70037_1_.getUUID("FollowingPlayer");
      PlayerEntity playerByUUID = this.level.getPlayerByUUID(followingPlayerUUID);
      if(playerByUUID != null) {
         this.followingPlayer = playerByUUID;
      }
   }

   public void playerTouch(PlayerEntity playerEntity) {
      if (!this.level.isClientSide) {
         if (this.floatTime == 0) {
            //Throw OrbEvent
            //if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerXpEvent.PickupXp(playerEntity, this))) return;
//            playerEntity.take(this, 1); TODO: Is this needed?

            if (this.value > 0) {
               ISoulCaster soulCasterCapability = SoulCasterHelper.getSoulCasterCapability(playerEntity);
               if(soulCasterCapability != null){
                  soulCasterCapability.addSouls(this.value, playerEntity);
               }
            }

            this.remove();
         }

      }
   }

   public float getValue() {
      return this.value;
   }

   public int getIcon() {
      if (this.value >= 2477) {
         return 10;
      } else if (this.value >= 1237) {
         return 9;
      } else if (this.value >= 617) {
         return 8;
      } else if (this.value >= 307) {
         return 7;
      } else if (this.value >= 149) {
         return 6;
      } else if (this.value >= 73) {
         return 5;
      } else if (this.value >= 37) {
         return 4;
      } else if (this.value >= 17) {
         return 3;
      } else if (this.value >= 7) {
         return 2;
      } else {
         return this.value >= 3 ? 1 : 0;
      }
   }

   public static int getExperienceValue(int p_70527_0_) {
      if (p_70527_0_ >= 2477) {
         return 2477;
      } else if (p_70527_0_ >= 1237) {
         return 1237;
      } else if (p_70527_0_ >= 617) {
         return 617;
      } else if (p_70527_0_ >= 307) {
         return 307;
      } else if (p_70527_0_ >= 149) {
         return 149;
      } else if (p_70527_0_ >= 73) {
         return 73;
      } else if (p_70527_0_ >= 37) {
         return 37;
      } else if (p_70527_0_ >= 17) {
         return 17;
      } else if (p_70527_0_ >= 7) {
         return 7;
      } else {
         return p_70527_0_ >= 3 ? 3 : 1;
      }
   }

   public boolean isAttackable() {
      return false;
   }

   @Override
   public IPacket<?> getAddEntityPacket() {
      return NetworkHooks.getEntitySpawningPacket(this);
   }

   @Override
   public void writeSpawnData(PacketBuffer buffer) {
      buffer.writeInt(this.health);
      buffer.writeInt(this.age);
      buffer.writeFloat(this.value);
      buffer.writeUUID(this.followingPlayer.getUUID());
   }

   @Override
   public void readSpawnData(PacketBuffer additionalData) {
      this.health = additionalData.readInt();
      this.age = additionalData.readInt();
      this.value = additionalData.readFloat();
      PlayerEntity playerByUUID = this.level.getPlayerByUUID(additionalData.readUUID());
      if(playerByUUID != null) {
         this.followingPlayer = playerByUUID;
      }
   }
}

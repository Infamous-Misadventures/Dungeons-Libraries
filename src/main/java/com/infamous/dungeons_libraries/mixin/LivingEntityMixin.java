package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.minionmaster.Follower;
import com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(method = "dropFromLootTable", at = @At("HEAD"), cancellable = true)
    private void cancelDropFromLootTableIfSummoned(DamageSource source, boolean hurtByPlayer, CallbackInfo ci){
        Follower cap = FollowerLeaderHelper.getFollowerCapability(this);
        if (cap.isSummon()) {
            ci.cancel();
        }
    }
}

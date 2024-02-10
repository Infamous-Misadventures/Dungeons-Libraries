package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BowItem;getPowerForTime(I)F"),
            method = "releaseUsing")
    private float libraries_releaseUsing_getPowerForTime(int useTime, Operation<Float> original, ItemStack itemStack, Level level, LivingEntity livingEntity, int useTimeRemaining) {
        return RangedAttackHelper.getBowArrowVelocity(livingEntity, itemStack, useTime);
    }

    @Inject(method = "releaseUsing",
            at = @At(value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/world/item/BowItem;customArrow(Lnet/minecraft/world/entity/projectile/AbstractArrow;)Lnet/minecraft/world/entity/projectile/AbstractArrow;",
                    shift = At.Shift.AFTER,
                    remap = false),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    public void setArrowDamage(ItemStack useStack, Level level, LivingEntity shooter, int useTimeRemaining, CallbackInfo ci,
                               Player playerShooter, boolean infiniteAmmo, ItemStack projectileStack, int useTime, float powerForTime, boolean isInfiniteArrow, ArrowItem arrowitem, AbstractArrow createdArrow) {
        RangedAttackHelper.multiplyRangedDamage(shooter, createdArrow);
    }

    @Inject(method = "releaseUsing",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILSOFT)
    private void createAdditionalArrows(ItemStack stack, Level level, LivingEntity shooter, int useTimeRemaining, CallbackInfo ci,
                                        Player playerShooter, boolean infiniteAmmo, ItemStack projectileStack, int useTime, float powerForTime, boolean isInfiniteArrow, ArrowItem arrowitem, AbstractArrow originalArrow){
        // Make some last minute changes to the original arrow
        if(powerForTime >= 1.0F){
            originalArrow.setCritArrow(true);
        }

        int piercingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack);
        if (piercingLevel > 0) {
            originalArrow.setPierceLevel((byte) piercingLevel);
        }

        // Finished making changes to original arrow - now to the multishot stuff!
        int multishotLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack);
        if(multishotLevel > 0){
            int additionalArrowCount = multishotLevel * 2;
            for(int arrowIndex = 1; arrowIndex <= additionalArrowCount; arrowIndex++){
                RangedAttackHelper.createBowArrow(stack, level, playerShooter, projectileStack, powerForTime, arrowIndex, isInfiniteArrow);
            }
        }
    }
}

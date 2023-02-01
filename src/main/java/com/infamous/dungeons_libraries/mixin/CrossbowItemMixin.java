package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {
    private static boolean LOGGED_WARNING = false;

    @Inject(method = "performShooting", at = @At("HEAD"), cancellable = true)
    private static void handlePerformShooting(Level level, LivingEntity shooter, InteractionHand useHand, ItemStack crossbow, float velocity, float inaccuracy, CallbackInfo ci) {
        ci.cancel();
        if(!LOGGED_WARNING){
            DungeonsLibraries.LOGGER.debug("CrossbowItem#performShooting was canceled by Dungeons Libraries and replaced with its CrossbowGear#fireCrossbowProjectiles");
            LOGGED_WARNING = true;
        }
        RangedAttackHelper.fireCrossbowProjectiles(level, shooter, useHand, crossbow, velocity, inaccuracy);
    }

    @Redirect(method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;getShootingPower(Lnet/minecraft/world/item/ItemStack;)F"))
    private float libraries_use_getShootingPower(ItemStack stack, Level level, Player player, InteractionHand hand){
        return RangedAttackHelper.getCrossbowArrowVelocity(player, stack);
    }

    @Inject(at = @At("RETURN"), method = "getChargeDuration", cancellable = true)
    private static void getChargeDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue((int) RangedAttackHelper.getVanillaCrossbowChargeTime(null, stack)); // TODO: Should take in a LivingEntity to be able to check for Roll Charge
    }

    @ModifyConstant(method = "tryLoadProjectiles", constant = @Constant(intValue = 3, ordinal = 0))
    private static int handleExtraMultishot(int defaultValue, LivingEntity livingEntity, ItemStack itemStack){
        return 1 + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, itemStack) * 2;
    }

    @Inject(method = "getArrow", at = @At(value = "RETURN"))
    private static void handleGetArrow(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, CallbackInfoReturnable<AbstractArrow> cir){
        AbstractArrow returnValue = cir.getReturnValue();
        RangedAttackHelper.multiplyRangedDamage(shooter, returnValue);

        // allow power, punch and flame to be applied to this arrow like bows
        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, weapon);
        if (powerLevel > 0) {
            returnValue.setBaseDamage(returnValue.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
        }

        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, weapon);
        if (punchLevel > 0) {
            returnValue.setKnockback(punchLevel);
        }

        int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, weapon);
        if (flameLevel > 0) {
            returnValue.setSecondsOnFire(100);
        }
    }
}

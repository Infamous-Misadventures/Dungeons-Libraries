package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.event.BowEvent;
import com.infamous.dungeons_libraries.items.gearconfig.BowGear;
import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGear;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import static net.minecraft.item.CrossbowItem.containsChargedProjectile;

public class RangedAttackHelper {

    public static float getVanillaArrowVelocity(LivingEntity livingEntity, ItemStack stack, int charge) {
        float bowChargeTime = RangedAttackHelper.getVanillaBowChargeTime(livingEntity, stack);
        if(bowChargeTime <= 0){
            bowChargeTime = 1;
        }
        float arrowVelocity = (float)charge / bowChargeTime;
        arrowVelocity = (arrowVelocity * arrowVelocity + arrowVelocity * 2.0F) / 3.0F;
        float velocityLimit = 1.0F;
        int overchargeLevel = 0; //EnchantmentHelper.getItemEnchantmentLevel(RangedEnchantmentList.OVERCHARGE, stack);
        if(overchargeLevel > 0){
            velocityLimit += overchargeLevel;
        }

        if (arrowVelocity > velocityLimit) {
            arrowVelocity = velocityLimit;
        }

        return arrowVelocity;
    }

    public static float getVanillaBowChargeTime(LivingEntity livingEntity, ItemStack stack){
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        BowEvent.ChargeTime event = new BowEvent.ChargeTime(livingEntity, stack, 20.0F);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
    }

    public static float getVanillaCrossbowChargeTime(LivingEntity livingEntity, ItemStack stack){
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        BowEvent.ChargeTime event = new BowEvent.ChargeTime(livingEntity, stack, 25.0F);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
    }

    public static float getModdedCrossbowChargeTime(LivingEntity livingEntity, ItemStack stack){
        float chargeTime;
        if(stack.getItem() instanceof CrossbowGear){
            chargeTime = ((CrossbowGear)stack.getItem()).getCrossbowChargeTime(livingEntity, stack);
        }
        else{
            chargeTime = 25;
        }
        return chargeTime;
    }

    public static float getModdedBowChargeTime(LivingEntity livingEntity, ItemStack stack){
        float chargeTime;
        if(stack.getItem() instanceof BowGear){
            chargeTime = ((BowGear)stack.getItem()).getBowChargeTime(livingEntity, stack);
        }
        else{
            chargeTime = 20.0F;
        }
        return chargeTime;
    }

    public static float getVanillaOrModdedCrossbowArrowVelocity(LivingEntity livingEntity, ItemStack stack) {
        float arrowVelocity;
        if(stack.getItem() instanceof CrossbowGear){
            arrowVelocity = CrossbowGear.getArrowVelocity(livingEntity, stack);
        }
        else{
            arrowVelocity = containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
        }
        return arrowVelocity;
    }

    public static float getVanillaOrModdedBowArrowVelocity(LivingEntity livingEntity, ItemStack stack, int charge) {
        float arrowVelocity;
        if(stack.getItem() instanceof BowGear){
            arrowVelocity = ((BowGear)stack.getItem()).getBowArrowVelocity(livingEntity, stack, charge);
        }
        else{
            arrowVelocity = getVanillaArrowVelocity(livingEntity, stack, charge);
        }
        return arrowVelocity;
    }
}

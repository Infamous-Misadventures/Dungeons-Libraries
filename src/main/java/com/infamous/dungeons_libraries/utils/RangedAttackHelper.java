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

    public static float getArrowVelocity(LivingEntity livingEntity, ItemStack stack, int charge) {
        float bowChargeTime = RangedAttackHelper.getBowChargeTime(livingEntity, stack);
        if(bowChargeTime <= 0){
            bowChargeTime = 1;
        }
        float arrowVelocity = (float)charge / bowChargeTime;
        arrowVelocity = (arrowVelocity * arrowVelocity + arrowVelocity * 2.0F) / 3.0F;
        float velocityLimit = 1.0F;
        BowEvent.Overcharge event = new BowEvent.Overcharge(livingEntity, stack, 0);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        int overchargeLevel = event.getCharges();
        if(overchargeLevel > 0){
            velocityLimit += overchargeLevel;
        }
        if (arrowVelocity > velocityLimit) {
            arrowVelocity = velocityLimit;
        }
        return arrowVelocity;
    }

    public static float getBowChargeTime(LivingEntity livingEntity, ItemStack stack){
        float defaultChargeTime = stack.getItem() instanceof BowGear ? ((BowGear)stack.getItem()).getDefaultChargeTime() : 20.0F;
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        BowEvent.ChargeTime event = new BowEvent.ChargeTime(livingEntity, stack, defaultChargeTime);
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
}

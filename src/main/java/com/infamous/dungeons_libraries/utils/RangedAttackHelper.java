package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.event.BowEvent;
import com.infamous.dungeons_libraries.event.CrossbowEvent;
import com.infamous.dungeons_libraries.items.gearconfig.BowGear;
import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGear;
import com.infamous.dungeons_libraries.mixin.CrossbowItemInvoker;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.RANGED_DAMAGE_MULTIPLIER;
import static net.minecraft.world.item.CrossbowItem.containsChargedProjectile;


public class RangedAttackHelper {

    public static float getBowArrowVelocity(LivingEntity livingEntity, ItemStack stack, int charge) {
        float bowChargeTime = RangedAttackHelper.getBowChargeTime(livingEntity, stack);
        if (bowChargeTime <= 0) {
            bowChargeTime = 1;
        }
        float arrowVelocity = (float) charge / bowChargeTime;
        arrowVelocity = (arrowVelocity * arrowVelocity + arrowVelocity * 2.0F) / 3.0F;
        float velocityLimit = 1.0F;
        BowEvent.Overcharge overchargeEvent = new BowEvent.Overcharge(livingEntity, stack, 0);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(overchargeEvent);
        int overchargeLevel = overchargeEvent.getCharges();
        if (overchargeLevel > 0) {
            velocityLimit += overchargeLevel;
        }
        if (arrowVelocity > velocityLimit) {
            arrowVelocity = velocityLimit;
        }

        BowEvent.Velocity velocityEvent = new BowEvent.Velocity(livingEntity, stack, arrowVelocity);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(velocityEvent);
        return velocityEvent.getVelocity();
    }

    public static float getBowChargeTime(LivingEntity livingEntity, ItemStack stack) {
        float defaultChargeTime = stack.getItem() instanceof BowGear ? ((BowGear) stack.getItem()).getDefaultChargeTime() : 20.0F;
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        BowEvent.ChargeTime event = new BowEvent.ChargeTime(livingEntity, stack, defaultChargeTime);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
    }

    public static float getVanillaCrossbowChargeTime(@Nullable LivingEntity livingEntity, ItemStack stack) {
        int quickChargeLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        float minTime = 1;
        CrossbowEvent.ChargeTime event = new CrossbowEvent.ChargeTime(livingEntity, stack, 25.0F);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return Math.max(event.getChargeTime() - 5 * quickChargeLevel, minTime);
    }

    public static float getCrossbowChargeTime(@Nullable LivingEntity livingEntity, ItemStack stack) {
        float chargeTime;
        if (stack.getItem() instanceof CrossbowGear crossbowGear) {
            chargeTime = crossbowGear.getCrossbowChargeTime(livingEntity, stack);
        } else {
            chargeTime = getVanillaCrossbowChargeTime(livingEntity, stack);
        }
        return chargeTime;
    }

    public static float getCrossbowArrowVelocity(@Nullable LivingEntity livingEntity, ItemStack stack) {
        float baseVelocity = 3.15F;
        if (containsChargedProjectile(stack, Items.FIREWORK_ROCKET)) {
            baseVelocity = 1.6F;
        }
        CrossbowEvent.Velocity event = new CrossbowEvent.Velocity(livingEntity, stack, baseVelocity);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        return event.getVelocity();
    }

    public static float getAngleForProjectileByIndex(int projectileIndex) {
        int indexScale = projectileIndex / 2;
        return 10.0F * (projectileIndex % 2 != 0 ? indexScale + 1 : indexScale * -1.0F);
    }

    public static void multiplyRangedDamage(LivingEntity shooter, AbstractArrow arrow) {
        AttributeInstance rangedDamageMultiplier = shooter.getAttribute(RANGED_DAMAGE_MULTIPLIER.get());
        if (rangedDamageMultiplier != null) {
            arrow.setBaseDamage(arrow.getBaseDamage() * (rangedDamageMultiplier.getValue()));
        }
    }

    public static void createBowArrow(ItemStack bowStack, Level world, Player player, ItemStack projectileStack, float powerForTime, int arrowIndex, boolean isInfiniteArrow) {
        ArrowItem arrowitem = (ArrowItem) (projectileStack.getItem() instanceof ArrowItem ? projectileStack.getItem() : Items.ARROW);
        AbstractArrow arrow = arrowitem.createArrow(world, projectileStack, player);
        if(bowStack.getItem() instanceof BowItem bowItem) bowItem.customArrow(arrow);
        multiplyRangedDamage(player, arrow);
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot() + getAngleForProjectileByIndex(arrowIndex), 0.0F, powerForTime * 3.0F, 1.0F);

        if (powerForTime >= 1.0F) {
            arrow.setCritArrow(true);
        }
        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
        if (powerLevel > 0) {
            arrow.setBaseDamage(arrow.getBaseDamage() + (double) powerLevel * 0.5D + 0.5D);
        }

        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
        if (punchLevel > 0) {
            arrow.setKnockback(punchLevel);
        }

        int flameLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack);
        if (flameLevel > 0) {
            arrow.setSecondsOnFire(100);
        }

        int piercingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, bowStack);
        if (piercingLevel > 0) {
            arrow.setPierceLevel((byte) piercingLevel);
        }

        bowStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
        if (isInfiniteArrow
                || player.getAbilities().instabuild && isSpecialArrow(projectileStack)
                || arrowIndex > 0) {
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        world.addFreshEntity(arrow);
    }

    private static boolean isSpecialArrow(ItemStack projectileStack) {
        return projectileStack.is(Items.SPECTRAL_ARROW) || projectileStack.is(Items.TIPPED_ARROW);
    }

    public static void fireCrossbowProjectiles(Level world, LivingEntity livingEntity, InteractionHand hand, ItemStack stack, float velocityIn, float inaccuracyIn) {
        if (livingEntity instanceof Player player && ForgeEventFactory.onArrowLoose(stack, livingEntity.level, player, 1, true) < 0) return;

        List<ItemStack> list = CrossbowItemInvoker.callGetChargedProjectiles(stack);
        float[] randomSoundPitches = CrossbowItemInvoker.callGetShotPitches(livingEntity.getRandom());

        for (int projectileIndex = 0; projectileIndex < list.size(); ++projectileIndex) {
            ItemStack currentProjectile = list.get(projectileIndex);
            boolean playerInCreativeMode = livingEntity instanceof Player && ((Player) livingEntity).getAbilities().instabuild;
            if (!currentProjectile.isEmpty()) {
                float projectileAngle = getAngleForProjectileByIndex(projectileIndex);
                CrossbowItemInvoker.callShootProjectile(world, livingEntity, hand, stack, currentProjectile, randomSoundPitches[projectileIndex == 0 ? 0 : projectileIndex % 2 != 0 ? 1 : 2], playerInCreativeMode, velocityIn, inaccuracyIn, projectileAngle);
            }
        }

        CrossbowItemInvoker.callOnCrossbowShot(world, livingEntity, stack);
    }
}

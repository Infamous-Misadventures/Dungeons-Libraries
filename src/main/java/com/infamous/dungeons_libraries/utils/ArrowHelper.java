package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectile;
import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectileHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class ArrowHelper {

    @SubscribeEvent
    public static void onArrowJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AbstractArrow) {
            AbstractArrow arrowEntity = (AbstractArrow) event.getEntity();
            //if(arrowEntity.getTags().contains("BonusProjectile") || arrowEntity.getTags().contains("ChainReactionProjectile")) return;
            Entity shooterEntity = arrowEntity.getOwner();
            if (shooterEntity instanceof LivingEntity) {
                LivingEntity shooter = (LivingEntity) shooterEntity;
                ItemStack mainhandStack = shooter.getMainHandItem();
                ItemStack offhandStack = shooter.getOffhandItem();
                // This should guarantee the arrow came from the correct itemstack
                if (mainhandStack.getItem() instanceof BowItem || mainhandStack.getItem() instanceof CrossbowItem) {
                    handleRangedEnchantments(arrowEntity, shooter, mainhandStack);
                } else if (offhandStack.getItem() instanceof BowItem || offhandStack.getItem() instanceof CrossbowItem) {
                    handleRangedEnchantments(arrowEntity, shooter, offhandStack);
                }
            }
        }
    }

    private static void handleRangedEnchantments(AbstractArrow arrowEntity, LivingEntity shooter, ItemStack stack) {
        addEnchantmentTagsToArrow(stack, arrowEntity);

//        int fuseShotLevel = EnchantmentHelper.getItemEnchantmentLevel(RangedEnchantmentList.FUSE_SHOT, stack);
//        if (hasFuseShotBuiltIn(stack)) fuseShotLevel++;
//        if (fuseShotLevel > 0) {
//            IBow weaponCap = CapabilityHelper.getWeaponCapability(stack);
//            int fuseShotCounter = weaponCap.getFuseShotCounter();
//            // 6 - 1, 6 - 2, 6 - 3
//            // zero indexing, so subtract 1 as well
//            if (fuseShotCounter == 6 - fuseShotLevel - 1) {
//                arrowEntity.addTag(FuseShotEnchantment.FUSE_SHOT_TAG);
//                weaponCap.setFuseShotCounter(0);
//            } else {
//                weaponCap.setFuseShotCounter(fuseShotCounter + 1);
//            }
//        }
//
//        if (shooter instanceof Player) {
//            Player playerEntity = (Player) shooter;
//            boolean soulsCriticalBoost = ProjectileEffectHelper.soulsCriticalBoost(playerEntity, stack);
//            if (soulsCriticalBoost) {
//                PROXY.spawnParticles(playerEntity, ParticleTypes.SOUL);
//                arrowEntity.setCritArrow(true);
//                arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() * 2);
//            }
//        }
    }

    public static boolean hasEnchantment(ItemStack stack, Enchantment enchantment){
        return enchantment != null && EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack) > 0;
    }

    public static boolean hasEnchantment(LivingEntity entity, Enchantment enchantment) {
        return enchantment != null && EnchantmentHelper.getEnchantmentLevel(enchantment, entity) > 0;
    }

    public static boolean shooterIsLiving(AbstractArrow arrowEntity) {
        return arrowEntity.getOwner() != null && arrowEntity.getOwner() instanceof LivingEntity;
    }

    public static boolean arrowHitLivingEntity(HitResult hitResult) {
        if(hitResult instanceof EntityHitResult){
            EntityHitResult entityHitResult = (EntityHitResult)hitResult;
            if(entityHitResult.getEntity() instanceof LivingEntity){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static boolean arrowHitMob(HitResult hitResult) {
        if(hitResult instanceof EntityHitResult entityHitResult){
            return entityHitResult.getEntity() instanceof Mob;
        } else{
            return false;
        }
    }

    public static void addEnchantmentTagsToArrow(ItemStack itemStack, Projectile projectileEntity){
        EnchantedProjectile cap = EnchantedProjectileHelper.getEnchantedProjectileCapability(projectileEntity);
        cap.setEnchantments(itemStack);
    }

    public static int enchantmentTagToLevel(Projectile projectileEntity, Enchantment enchantment){
        EnchantedProjectile cap = EnchantedProjectileHelper.getEnchantedProjectileCapability(projectileEntity);
        return cap.getEnchantmentLevel(enchantment);
    }
}

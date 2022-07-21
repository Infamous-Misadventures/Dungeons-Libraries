package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectile;
import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectileHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class ArrowHelper {

    @SubscribeEvent
    public static void onArrowJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AbstractArrowEntity) {
            AbstractArrowEntity arrowEntity = (AbstractArrowEntity) event.getEntity();
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

    private static void handleRangedEnchantments(AbstractArrowEntity arrowEntity, LivingEntity shooter, ItemStack stack) {
        addEnchantmentTagsToArrow(stack, arrowEntity);

//        int fuseShotLevel = EnchantmentHelper.getItemEnchantmentLevel(RangedEnchantmentList.FUSE_SHOT, stack);
//        if (hasFuseShotBuiltIn(stack)) fuseShotLevel++;
//        if (fuseShotLevel > 0) {
//            IBow weaponCap = CapabilityHelper.getWeaponCapability(stack);
//            if (weaponCap == null) return;
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
//        if (shooter instanceof PlayerEntity) {
//            PlayerEntity playerEntity = (PlayerEntity) shooter;
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

    public static boolean shooterIsLiving(AbstractArrowEntity arrowEntity) {
        return arrowEntity.getOwner() != null && arrowEntity.getOwner() instanceof LivingEntity;
    }

    public static boolean arrowHitLivingEntity(RayTraceResult rayTraceResult) {
        if(rayTraceResult instanceof EntityRayTraceResult){
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
            if(entityRayTraceResult.getEntity() instanceof LivingEntity){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static boolean arrowHitMob(RayTraceResult rayTraceResult) {
        if(rayTraceResult instanceof EntityRayTraceResult){
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)rayTraceResult;
            if(entityRayTraceResult.getEntity() instanceof MobEntity){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static void addEnchantmentTagsToArrow(ItemStack itemStack, ProjectileEntity projectileEntity){
        EnchantedProjectile cap = EnchantedProjectileHelper.getEnchantedProjectileCapability(projectileEntity);
        if(cap != null){
            cap.setEnchantments(itemStack);
        }
    }

    public static int enchantmentTagToLevel(ProjectileEntity projectileEntity, Enchantment enchantment){
        EnchantedProjectile cap = EnchantedProjectileHelper.getEnchantedProjectileCapability(projectileEntity);
        if(cap != null){
            return cap.getEnchantmentLevel(enchantment);
        }else {
            return 0;
        }
    }
}

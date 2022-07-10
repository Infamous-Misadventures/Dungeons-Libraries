package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
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
        if(hitResult instanceof EntityHitResult){
            EntityHitResult entityHitResult = (EntityHitResult)hitResult;
            if(entityHitResult.getEntity() instanceof Mob){
                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public static void addEnchantmentTagsToArrow(ItemStack itemStack, AbstractArrow arrowEntity){
        Map<ResourceLocation, Integer> enchantments = itemStack.getEnchantmentTags().stream().collect(Collectors.toMap(inbt -> ResourceLocation.tryParse(((CompoundTag) inbt).getString("id")), inbt -> ((CompoundTag) inbt).getInt("lvl")));
        Set<ResourceLocation> resourceLocations = enchantments.keySet();
        LazyOptional<BuiltInEnchantments> lazyCap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapabilityLazy(itemStack);
        if(lazyCap.isPresent()){
            Map<ResourceLocation, Integer> builtInEnchantments = lazyCap.resolve().get().getAllBuiltInEnchantmentInstances().stream()
                    .filter(enchantmentInstance -> !resourceLocations.contains(enchantmentInstance.enchantment.getRegistryName()))
                    .collect(Collectors.groupingBy(enchantmentInstance -> enchantmentInstance.enchantment.getRegistryName(), Collectors.summingInt(value -> value.level)));
            enchantments = Stream.concat(enchantments.entrySet().stream(), builtInEnchantments.entrySet().stream())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (value1, value2) -> value1 + value2));
        }
        enchantments.forEach((resourceLocation, integer) -> {
            String enchantmentTag = resourceLocation.toString() + integer;
            arrowEntity.addTag(enchantmentTag);
        });
    }

    public static int enchantmentTagToLevel(AbstractArrow arrowEntity, Enchantment enchantment){
        String enchantmentAsString = enchantment.getRegistryName().toString();
        int maxLevel = enchantment.getMaxLevel()+10;
        for(int i = 1; i < maxLevel + 1; i++){
            String enchantmentTag = enchantmentAsString + i;
            if(arrowEntity.getTags().contains(enchantmentTag)){
                return i;
            }
        }
        return 0;
    }
}

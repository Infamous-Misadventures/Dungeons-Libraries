package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.RANGED_DAMAGE_MULTIPLIER;

@Mixin(BowItem.class)
public class BowItemMixin {

//    @ModifyVariable(at = @At("STORE"), method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V")
//    private float libraries_releaseUsing_setArrowVelocity(float arrowVelocity, ItemStack stack, Level worldIn, LivingEntity livingEntity, int timeLeft) {
//        Player playerentity = (Player)livingEntity;
//
//        boolean flag = playerentity.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
//        ItemStack itemstack = playerentity.getProjectile(stack);
//
//        int i = 72000 - timeLeft;
//        i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
//        if (i < 0) return 0;
//
//        arrowVelocity = RangedAttackHelper.getArrowVelocity(livingEntity, stack, i);
//        return arrowVelocity;
//    }

    @Inject(method = "releaseUsing(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/BowItem;customArrow(Lnet/minecraft/world/entity/projectile/AbstractArrow;)Lnet/minecraft/world/entity/projectile/AbstractArrow;", remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void libraries_releaseUsing_setArrowDamage(ItemStack pStack, Level j, LivingEntity k, int pTimeLeft, CallbackInfo ci, Player playerentity, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrow abstractArrowEntity) {
        AttributeInstance attribute = playerentity.getAttribute(RANGED_DAMAGE_MULTIPLIER.get());
        if(attribute != null) {
            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() * (attribute.getValue() + 1));
        }
    }
}

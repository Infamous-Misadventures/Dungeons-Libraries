package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.RANGED_DAMAGE_MULTIPLIER;

@Mixin(BowItem.class)
public class BowItemMixin {

    @ModifyVariable(at = @At("STORE"), method = "releaseUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V")
    private float libraries_releaseUsing_setArrowVelocity(float arrowVelocity, ItemStack stack, World worldIn, LivingEntity livingEntity, int timeLeft) {
        PlayerEntity playerentity = (PlayerEntity)livingEntity;

        boolean flag = playerentity.abilities.instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
        ItemStack itemstack = playerentity.getProjectile(stack);

        int i = 72000 - timeLeft;
        i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
        if (i < 0) return 0;

        arrowVelocity = RangedAttackHelper.getArrowVelocity(livingEntity, stack, i);
        return arrowVelocity;
    }

    @Inject(method = "Lnet/minecraft/item/BowItem;releaseUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/BowItem;customArrow(Lnet/minecraft/entity/projectile/AbstractArrowEntity;)Lnet/minecraft/entity/projectile/AbstractArrowEntity;", remap = false), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void libraries_releaseUsing_setArrowDamage(ItemStack pStack, World j, LivingEntity k, int pTimeLeft, CallbackInfo ci, PlayerEntity playerentity, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrowEntity abstractArrowEntity) {
        ModifiableAttributeInstance attribute = playerentity.getAttribute(RANGED_DAMAGE_MULTIPLIER.get());
        if(attribute != null) {
            abstractArrowEntity.setBaseDamage(abstractArrowEntity.getBaseDamage() * (attribute.getValue()));
        }
    }
}

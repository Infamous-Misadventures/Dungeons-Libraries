package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGear;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Shadow
    public static void performShooting(Level worldIn, LivingEntity shooter, InteractionHand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
    }

//    @Inject(at = @At("RETURN"), method = "getChargeDuration(Lnet/minecraft/item/ItemStack;)I", cancellable = true)
//    private static void getChargeDuration(ItemStack stack, CallbackInfoReturnable<Integer> cir){
//        cir.setReturnValue(RangedAttackHelper.getVanillaCrossbowChargeTime(stack)); // TODO: Should take in a LivingEntity to be able to check for Roll Charge
//    }

    @Redirect(at=@At(value = "INVOKE", target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FF)V"), method = "use")
    private void hack(Level worldIn, LivingEntity shooter, InteractionHand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
        if(stack.getItem() instanceof CrossbowGear){
            ((CrossbowGear)stack.getItem()).fireCrossbowProjectiles(worldIn, shooter, handIn, stack, velocityIn, inaccuracyIn);
        } else performShooting(worldIn, shooter, handIn, stack, velocityIn, inaccuracyIn);
    }
}

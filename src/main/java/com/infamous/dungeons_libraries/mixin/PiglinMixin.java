package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Piglin.class)
public class PiglinMixin {

    @Inject(at = @At("RETURN"), method = "canFireProjectileWeapon", cancellable = true)
    private void handleCanFireProjectileWeapon(ProjectileWeaponItem pProjectileWeapon, CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(pProjectileWeapon instanceof CrossbowItem);
    }

    @Redirect(method = "canReplaceCurrentItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"))
    private boolean handleIsCrossbow(ItemStack instance, Item pItem){
        return instance.getItem() instanceof CrossbowItem;
    }
}

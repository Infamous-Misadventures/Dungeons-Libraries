package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.items.gearconfig.MeleeGear;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mob.class)
public abstract class MobMixin {

    @Shadow
    public abstract boolean canReplaceEqualItem(ItemStack pCandidate, ItemStack pExisting);

    @Inject(method = "Lnet/minecraft/world/entity/Mob;canReplaceCurrentItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void dungeonslibraries_canReplaceCurrentItem(ItemStack pCandidate, ItemStack pExisting, CallbackInfoReturnable<Boolean> cir) {
        if (pCandidate.getItem() instanceof SwordItem) {
            if (!(pExisting.getItem() instanceof SwordItem) && !(pExisting.getItem() instanceof MeleeGear)) {
                cir.setReturnValue(true);
            } else if (pExisting.getItem() instanceof MeleeGear) {
                SwordItem sworditem = (SwordItem) pCandidate.getItem();
                MeleeGear meleeGear = (MeleeGear) pExisting.getItem();
                if (sworditem.getDamage() != meleeGear.getDamage()) {
                    cir.setReturnValue(sworditem.getDamage() > meleeGear.getDamage());
                } else {
                    cir.setReturnValue(canReplaceEqualItem(pCandidate, pExisting));
                }
            }
        } else if (pCandidate.getItem() instanceof MeleeGear) {
            if (!(pExisting.getItem() instanceof SwordItem) && !(pExisting.getItem() instanceof MeleeGear)) {
                cir.setReturnValue(true);
            } else if (pExisting.getItem() instanceof SwordItem) {
                SwordItem sworditem = (SwordItem) pExisting.getItem();
                MeleeGear meleeGear = (MeleeGear) pCandidate.getItem();
                if (sworditem.getDamage() != meleeGear.getDamage()) {
                    cir.setReturnValue(sworditem.getDamage() > meleeGear.getDamage());
                } else {
                    cir.setReturnValue(canReplaceEqualItem(pCandidate, pExisting));
                }
            }
        }
    }

}

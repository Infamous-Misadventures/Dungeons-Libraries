package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(CrossbowItem.class)
public interface CrossbowItemInvoker {

    @Invoker("addChargedProjectile")
    static void addChargedProjectile(ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker("getChargedProjectiles")
    static List<ItemStack> getChargedProjectiles(ItemStack pCrossbowStack) {
        throw new RuntimeException("Invoker failed to mixin");
    }

    @Invoker("onCrossbowShot")
    static void onCrossbowShot(Level pLevel, LivingEntity pShooter, ItemStack pCrossbowStack) {
        throw new RuntimeException("Invoker failed to mixin");
    }
}

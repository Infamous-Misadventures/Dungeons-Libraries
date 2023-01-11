package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.mixinhandler.EnchantmentHelperMixinHandler;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    @Inject(method = "getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void dungeonslibraries_getItemEnchantmentLevelEnchantmentFound(Enchantment enchantment, ItemStack itemStack, CallbackInfoReturnable<Integer> cir, ResourceLocation enchantmentRL, ListTag listNbt, int i, CompoundTag compoundnbt, ResourceLocation found) {
        int currentLvl = Mth.clamp(compoundnbt.getInt("lvl"), 0, 255);
        BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
        Integer reduce = cap.getAllBuiltInEnchantmentInstances().stream()
                .filter(enchantmentInstance -> enchantmentInstance.enchantment == enchantment)
                .map(enchantmentInstance -> enchantmentInstance.level)
                .reduce(0, Integer::sum);
        cir.setReturnValue(currentLvl + reduce);
    }

    @Inject(method = "getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I",
            at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static void dungeonslibraries_getItemEnchantmentLevelEnchantmentNotFound(Enchantment enchantment, ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        int currentLvl = 0;
        BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
        Integer reduce = cap.getAllBuiltInEnchantmentInstances().stream()
                .filter(enchantmentInstance -> enchantmentInstance.enchantment == enchantment)
                .map(enchantmentInstance -> enchantmentInstance.level)
                .reduce(0, Integer::sum);
        cir.setReturnValue(currentLvl + reduce);
    }

    @Redirect(
            method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V")
    )
    private static void dungeonslibraries_runIterationOnItem2(Optional<Enchantment> instance, Consumer<Enchantment> action, EnchantmentHelper.EnchantmentVisitor visitor, ItemStack itemStack) {
        BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
        if (instance.isPresent()) {
            Integer reduce = cap.getAllBuiltInEnchantmentInstances().stream()
                    .filter(enchantmentInstance -> enchantmentInstance.enchantment == instance.get())
                    .map(enchantmentInstance -> enchantmentInstance.level)
                    .reduce(0, Integer::sum);
            visitor.accept(instance.get(), EnchantmentHelper.getItemEnchantmentLevel(instance.get(), itemStack) + reduce);
        }
    }

    @Inject(method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL"))
    private static void dungeonslibraries_runIterationOnItemWhenOnlyBuiltIn(EnchantmentHelper.EnchantmentVisitor visitor, ItemStack itemStack, CallbackInfo ci) {
        EnchantmentHelperMixinHandler.handler(visitor, itemStack);
    }

}

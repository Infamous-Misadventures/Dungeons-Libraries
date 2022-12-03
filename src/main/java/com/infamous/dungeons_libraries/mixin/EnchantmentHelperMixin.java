package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.extensions.IForgeItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentId;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    private static Optional<Enchantment> enchantmentOnIteration = null;
    private static ItemStack itemStackOnIteration = null;

    @Inject(method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getTagEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I" , remap = false,
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

    @Inject(method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getTagEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I", remap = false,
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


    @Redirect(method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getAllEnchantments()Ljava/util/Map;", remap = false), remap = false)
    private static Map dungeonslibraries_getAllEnchantments(ItemStack itemStack) {
        Map<Enchantment, Integer> enchantmentMap = itemStack.getAllEnchantments();
        Map<Enchantment, Integer> newEnchantmentMap = new HashMap<>(enchantmentMap);
        BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
        cap.getAllBuiltInEnchantmentInstances().forEach(
                enchantmentInstance -> newEnchantmentMap.compute(enchantmentInstance.enchantment, (enchantment, integer) -> {
                    if (integer == null) {
                        return enchantmentInstance.level;
                    } else {
                        return integer + enchantmentInstance.level;
                    }
                })
        );
        return newEnchantmentMap;
    }

//    @Inject(method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
//            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentId(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/resources/ResourceLocation;"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private static void dungeonslibraries_runIterationOnItemCapture(EnchantmentHelper.EnchantmentVisitor p_77518_0_, ItemStack p_77518_1_, CallbackInfo ci, ListTag listNBT, int i, CompoundTag compoundtag) {
//        enchantmentOnIteration = Registry.ENCHANTMENT.getOptional(getEnchantmentId(compoundtag));
//        itemStackOnIteration = p_77518_1_;
//    }
//
//    @Inject(
//            method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/nbt/CompoundTag;)I",
//            at = @At(value = "RETURN"), cancellable = true)
//    private static void dungeonslibraries_runIterationOnItem(CompoundTag value, CallbackInfoReturnable<Integer> cir) {
//        if (enchantmentOnIteration == null || itemStackOnIteration == null) {
//            return;
//        }
//        BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStackOnIteration);
//        if (enchantmentOnIteration.isPresent()) {
//            Integer reduce = cap.getAllBuiltInEnchantmentInstances().stream()
//                    .filter(enchantmentInstance -> enchantmentInstance.enchantment == enchantmentOnIteration.get())
//                    .map(enchantmentInstance -> enchantmentInstance.level)
//                    .reduce(0, Integer::sum);
//            cir.setReturnValue(reduce + cir.getReturnValue());
//        }
//        enchantmentOnIteration = null;
//        itemStackOnIteration = null;
//    }

//    @Inject(method = "runIterationOnItem(Lnet/minecraft/world/item/enchantment/EnchantmentHelper$EnchantmentVisitor;Lnet/minecraft/world/item/ItemStack;)V",
//            at = @At("TAIL"))
//    private static void dungeonslibraries_runIterationOnItemWhenOnlyBuiltIn(EnchantmentHelper.EnchantmentVisitor visitor, ItemStack itemStack, CallbackInfo ci) {
//        EnchantmentHelperMixinHandler.handler(visitor, itemStack);
//    }

}

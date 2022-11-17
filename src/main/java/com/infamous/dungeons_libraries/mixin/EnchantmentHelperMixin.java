package com.infamous.dungeons_libraries.mixin;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import com.infamous.dungeons_libraries.mixinhandler.EnchantmentHelperMixinHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

import static com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY;

//ToDo: runIterationOnItem: What if not present as another enchant?!
@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelperMixin {

    private static Optional<Enchantment> enchantmentOnIteration = null;
    private static ItemStack itemStackOnIteration = null;

    @Inject(method = "Lnet/minecraft/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I",
            at = @At(value = "RETURN", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void dungeonslibraries_getItemEnchantmentLevelEnchantmentFound(Enchantment enchantment, ItemStack itemStack, CallbackInfoReturnable<Integer> cir, ResourceLocation enchantmentRL, ListNBT listNbt, int i, CompoundNBT compoundnbt, ResourceLocation found) {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) return;
        int currentLvl = MathHelper.clamp(compoundnbt.getInt("lvl"), 0, 255);
        IBuiltInEnchantments iBuiltInEnchantments = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
        Integer reduce = iBuiltInEnchantments.getAllBuiltInEnchantmentDatas().stream()
                .filter(enchantmentData -> enchantmentData.enchantment == enchantment)
                .map(enchantmentData -> enchantmentData.level)
                .reduce(0, Integer::sum);
        cir.setReturnValue(currentLvl + reduce);
    }

    @Inject(method = "getItemEnchantmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I",
            at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static void dungeonslibraries_getItemEnchantmentLevelEnchantmentNotFound(Enchantment enchantment, ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) return;
        int currentLvl = 0;
        IBuiltInEnchantments iBuiltInEnchantments = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
        Integer reduce = iBuiltInEnchantments.getAllBuiltInEnchantmentDatas().stream()
                .filter(enchantmentData -> enchantmentData.enchantment == enchantment)
                .map(enchantmentData -> enchantmentData.level)
                .reduce(0, Integer::sum);
        cir.setReturnValue(currentLvl+reduce);
    }

    @Inject(method = "runIterationOnItem(Lnet/minecraft/enchantment/EnchantmentHelper$IEnchantmentVisitor;Lnet/minecraft/item/ItemStack;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundNBT;getString(Ljava/lang/String;)Ljava/lang/String;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void dungeonslibraries_runIterationOnItemCapture(EnchantmentHelper.IEnchantmentVisitor p_77518_0_, ItemStack p_77518_1_, CallbackInfo ci, ListNBT listNBT, int i, String s) {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) return;
        enchantmentOnIteration = Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(s));
        itemStackOnIteration = p_77518_1_;
    }

    @ModifyVariable(
            method = "runIterationOnItem(Lnet/minecraft/enchantment/EnchantmentHelper$IEnchantmentVisitor;Lnet/minecraft/item/ItemStack;)V",
            at = @At(value = "STORE"), ordinal = 1)
    private static int dungeonslibraries_runIterationOnItem(int j) {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) return j;
        if(enchantmentOnIteration.isPresent()){
            IBuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStackOnIteration);
            Integer reduce = cap.getAllBuiltInEnchantmentDatas().stream()
                    .filter(enchantmentData -> enchantmentData.enchantment == enchantmentOnIteration.get())
                    .map(enchantmentData -> enchantmentData.level)
                    .reduce(0, Integer::sum);
            return reduce + j;
        }
        return j;
    }

    @Inject(method = "runIterationOnItem(Lnet/minecraft/enchantment/EnchantmentHelper$IEnchantmentVisitor;Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL"))
    private static void dungeonslibraries_runIterationOnItemWhenOnlyBuiltIn(EnchantmentHelper.IEnchantmentVisitor visitor, ItemStack itemStack, CallbackInfo ci) {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) return;
        EnchantmentHelperMixinHandler.handler(visitor, itemStack);
    }

}

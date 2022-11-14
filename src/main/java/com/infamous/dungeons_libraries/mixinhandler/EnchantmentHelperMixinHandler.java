package com.infamous.dungeons_libraries.mixinhandler;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentHelperMixinHandler {
    public static void handler(EnchantmentHelper.IEnchantmentVisitor visitor, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            List<String> itemStackEnchantments = itemStack.getEnchantmentTags().stream().map(inbt -> ((CompoundNBT) inbt).getString("id")).collect(Collectors.toList());
            IBuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
            cap.getAllBuiltInEnchantmentDatas().stream()
                    .filter(enchantmentData -> !itemStackEnchantments.contains(enchantmentData.enchantment.getRegistryName().toString()))
                    .collect(Collectors.groupingBy(enchantmentData -> enchantmentData.enchantment, Collectors.summingInt(value -> value.level)))
                    .forEach(visitor::accept);
        }
    }
}

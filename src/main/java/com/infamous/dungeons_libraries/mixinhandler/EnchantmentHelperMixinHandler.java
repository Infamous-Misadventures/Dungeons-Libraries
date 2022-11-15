package com.infamous.dungeons_libraries.mixinhandler;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.stream.Collectors;

public class EnchantmentHelperMixinHandler {
    public static void handler(EnchantmentHelper.EnchantmentVisitor visitor, ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            List<String> itemStackEnchantments = itemStack.getEnchantmentTags().stream().map(inbt -> ((CompoundTag) inbt).getString("id")).collect(Collectors.toList());
            BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(itemStack);
            lazyCap.ifPresent(cap -> cap.getAllBuiltInEnchantmentInstances().stream()
                    .filter(enchantmentInstance -> !itemStackEnchantments.contains(enchantmentInstance.enchantment.getRegistryName().toString()))
                    .collect(Collectors.groupingBy(enchantmentInstance -> enchantmentInstance.enchantment, Collectors.summingInt(value -> value.level)))
                    .forEach(visitor::accept));
        }
    }
}

package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY;

public class BuiltInEnchantmentsHelper {

    public static LazyOptional<IBuiltInEnchantments> getBuiltInEnchantmentsCapabilityLazy(ItemStack itemStack)
    {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<IBuiltInEnchantments> lazyCap = itemStack.getCapability(BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY);
        return lazyCap;
    }

    public static IBuiltInEnchantments getBuiltInEnchantmentsCapability(ItemStack itemStack)
    {
        LazyOptional<IBuiltInEnchantments> lazyCap = itemStack.getCapability(BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the BuiltInEnchantments capability from the ItemStack!"));
        }
        return null;
    }
}

package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.BUILT_IN_ENCHANTMENTS_CAPABILITY;


public class BuiltInEnchantmentsHelper {

    public static LazyOptional<BuiltInEnchantments> getBuiltInEnchantmentsCapabilityLazy(ItemStack itemStack)
    {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<BuiltInEnchantments> lazyCap = itemStack.getCapability(BUILT_IN_ENCHANTMENTS_CAPABILITY);
        return lazyCap;
    }

    public static BuiltInEnchantments getBuiltInEnchantmentsCapability(ItemStack itemStack)
    {
        LazyOptional<BuiltInEnchantments> lazyCap = itemStack.getCapability(BUILT_IN_ENCHANTMENTS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the BuiltInEnchantments capability from the ItemStack!"));
        }
        return null;
    }
}

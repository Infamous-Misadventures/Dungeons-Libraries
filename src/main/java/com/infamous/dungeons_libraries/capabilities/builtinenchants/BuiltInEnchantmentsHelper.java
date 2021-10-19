package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class BuiltInEnchantmentsHelper {

    public static LazyOptional<IBuiltInEnchantments> getBuiltInEnchantmentsCapabilityLazy(ItemStack itemStack)
    {
        LazyOptional<IBuiltInEnchantments> lazyCap = itemStack.getCapability(BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY);
        return lazyCap;
    }

    public static IBuiltInEnchantments getBuiltInEnchantmentsCapability(ItemStack itemStack)
    {
        LazyOptional<IBuiltInEnchantments> lazyCap = itemStack.getCapability(BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Enchantable capability from the Entity!"));
        }
        return null;
    }
}

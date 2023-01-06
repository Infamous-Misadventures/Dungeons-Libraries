package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.world.item.ItemStack;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.BUILT_IN_ENCHANTMENTS_CAPABILITY;


public class BuiltInEnchantmentsHelper {

    public static BuiltInEnchantments getBuiltInEnchantmentsCapability(ItemStack itemStack) {
        return itemStack.getCapability(BUILT_IN_ENCHANTMENTS_CAPABILITY).orElse(new BuiltInEnchantments(itemStack));
    }

}

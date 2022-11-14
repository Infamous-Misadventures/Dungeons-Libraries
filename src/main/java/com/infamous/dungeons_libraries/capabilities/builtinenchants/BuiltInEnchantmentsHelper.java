package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

public class BuiltInEnchantmentsHelper {

    public static IBuiltInEnchantments getBuiltInEnchantmentsCapability(ItemStack itemStack)
    {
        LazyOptional<IBuiltInEnchantments> lazyCap = itemStack.getCapability(BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY);
        return lazyCap.orElse(new BuiltInEnchantments(itemStack));
    }
}

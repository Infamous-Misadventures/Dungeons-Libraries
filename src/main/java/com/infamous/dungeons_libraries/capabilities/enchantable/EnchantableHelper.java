package com.infamous.dungeons_libraries.capabilities.enchantable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.common.util.LazyOptional;

public class EnchantableHelper {

    public static LazyOptional<IEnchantable> getEnchantableCapabilityLazy(Entity entity)
    {
        LazyOptional<IEnchantable> lazyCap = entity.getCapability(EnchantableProvider.ENCHANTABLE_CAPABILITY);
        /*if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Enchantable capability from the Entity!"));
        }
        return null;*/
        return lazyCap;
    }

    public static IEnchantable getEnchantableCapability(Entity entity)
    {
        LazyOptional<IEnchantable> lazyCap = entity.getCapability(EnchantableProvider.ENCHANTABLE_CAPABILITY);
        if (lazyCap.isPresent()) {
            Enchantable enchantable = new Enchantable();
            enchantable.setSpawned(true);
            return lazyCap.orElse(enchantable);
        }
        return null;
    }

    public static boolean isEnchantableEntity(Entity object) {
        return object instanceof LivingEntity;
    }
}

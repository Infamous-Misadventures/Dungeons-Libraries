package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class EnchantedProjectileHelper {

    @Nullable
    public static EnchantedProjectile getEnchantedProjectileCapability(Entity entity)
    {
        LazyOptional<EnchantedProjectile> lazyCap = entity.getCapability(EnchantedProjectileProvider.ENCHANTED_PROJECTILE_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the combo capability from the Entity!"));
        }
        return null;
    }
}

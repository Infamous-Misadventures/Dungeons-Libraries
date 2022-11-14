package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class EnchantedProjectileHelper {

    @Nullable
    public static EnchantedProjectile getEnchantedProjectileCapability(Entity entity)
    {
        LazyOptional<EnchantedProjectile> lazyCap = entity.getCapability(EnchantedProjectileProvider.ENCHANTED_PROJECTILE_CAPABILITY);
        return lazyCap.orElse(new EnchantedProjectile());
    }
}

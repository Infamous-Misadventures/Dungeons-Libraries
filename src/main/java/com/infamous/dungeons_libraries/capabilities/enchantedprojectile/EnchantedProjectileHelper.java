package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class EnchantedProjectileHelper {

    public static EnchantedProjectile getEnchantedProjectileCapability(Entity entity)
    {
        return entity.getCapability(ModCapabilities.ENCHANTED_PROJECTILE_CAPABILITY).orElse(new EnchantedProjectile());
    }
}

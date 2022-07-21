package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class EnchantedProjectileHelper {

    @Nullable
    public static EnchantedProjectile getEnchantedProjectileCapability(Entity entity)
    {
        LazyOptional<EnchantedProjectile> lazyCap = entity.getCapability(ModCapabilities.ENCHANTED_PROJECTILE_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the combo capability from the Entity!"));
        }
        return null;
    }
}

package com.infamous.dungeons_libraries.capabilities.armored;

import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectile;
import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectileProvider;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class ArmoredMobHelper {

    @Nullable
    public static ArmoredMob getArmoredMobCapability(Entity entity)
    {
        LazyOptional<ArmoredMob> lazyCap = entity.getCapability(ArmoredMobProvider.ARMORED_MOB_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the combo capability from the Entity!"));
        }
        return null;
    }
}

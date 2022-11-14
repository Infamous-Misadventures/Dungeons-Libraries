package com.infamous.dungeons_libraries.capabilities.elite;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class EliteMobHelper {

    @Nullable
    public static EliteMob getEliteMobCapability(Entity entity)
    {
        LazyOptional<EliteMob> lazyCap = entity.getCapability(EliteMobProvider.ARMORED_MOB_CAPABILITY);
        return lazyCap.orElse(new EliteMob());
    }
}

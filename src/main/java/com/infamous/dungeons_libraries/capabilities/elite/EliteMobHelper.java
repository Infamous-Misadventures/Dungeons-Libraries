package com.infamous.dungeons_libraries.capabilities.elite;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.ELITE_MOB_CAPABILITY;

public class EliteMobHelper {

    public static EliteMob getEliteMobCapability(Entity entity)
    {
        return entity.getCapability(ELITE_MOB_CAPABILITY).orElse(new EliteMob());
    }
}

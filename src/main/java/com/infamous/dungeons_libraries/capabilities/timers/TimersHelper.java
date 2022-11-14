package com.infamous.dungeons_libraries.capabilities.timers;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.timers.TimersProvider.TIMERS_CAPABILITY;

public class TimersHelper {

    public static ITimers getTimersCapability(Entity entity)
    {
        LazyOptional<ITimers> lazyCap = entity.getCapability(TIMERS_CAPABILITY);
        return lazyCap.orElse(new Timers());
    }
}

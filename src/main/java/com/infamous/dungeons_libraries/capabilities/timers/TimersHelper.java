package com.infamous.dungeons_libraries.capabilities.timers;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.timers.TimersProvider.TIMERS_CAPABILITY;

public class TimersHelper {

    public static LazyOptional<ITimers> getTimersCapabilityLazy(Entity entity)
    {
        if(TIMERS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<ITimers> lazyCap = entity.getCapability(TIMERS_CAPABILITY);
        return lazyCap;
    }

    public static ITimers getTimersCapability(Entity entity)
    {
        LazyOptional<ITimers> lazyCap = entity.getCapability(TIMERS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Timers capability from the ItemStack!"));
        }
        return null;
    }
}

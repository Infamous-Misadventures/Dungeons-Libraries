package com.infamous.dungeons_libraries.capabilities.timers;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.TIMERS_CAPABILITY;


public class TimersHelper {

    public static LazyOptional<Timers> getTimersCapabilityLazy(Entity entity)
    {
        if(TIMERS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<Timers> lazyCap = entity.getCapability(TIMERS_CAPABILITY);
        return lazyCap;
    }

    public static Timers getTimersCapability(Entity entity)
    {
        LazyOptional<Timers> lazyCap = entity.getCapability(TIMERS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Timers capability from the ItemStack!"));
        }
        return null;
    }
}

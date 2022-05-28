package com.infamous.dungeons_libraries.capabilities.timers;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TimersProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ITimers.class)
    public static final Capability<ITimers> TIMERS_CAPABILITY = null;

    private LazyOptional<ITimers> instance = LazyOptional.of(TIMERS_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == TIMERS_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return TIMERS_CAPABILITY.getStorage().writeNBT(TIMERS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        TIMERS_CAPABILITY.getStorage().readNBT(TIMERS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
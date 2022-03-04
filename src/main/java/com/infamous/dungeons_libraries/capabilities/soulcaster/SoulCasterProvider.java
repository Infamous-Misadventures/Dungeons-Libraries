package com.infamous.dungeons_libraries.capabilities.soulcaster;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SoulCasterProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ISoulCaster.class)
    public static final Capability<ISoulCaster> SOUL_CASTER_CAPABILITY = null;

    private LazyOptional<ISoulCaster> instance = LazyOptional.of(SOUL_CASTER_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == SOUL_CASTER_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return SOUL_CASTER_CAPABILITY.getStorage().writeNBT(SOUL_CASTER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        SOUL_CASTER_CAPABILITY.getStorage().readNBT(SOUL_CASTER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
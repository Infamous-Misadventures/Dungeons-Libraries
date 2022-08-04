package com.infamous.dungeons_libraries.capabilities.armored;

import com.infamous.dungeons_libraries.capabilities.enchantedprojectile.EnchantedProjectile;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmoredMobProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(ArmoredMob.class)
    public static final Capability<ArmoredMob> ARMORED_MOB_CAPABILITY = null;

    private LazyOptional<ArmoredMob> instance = LazyOptional.of(ARMORED_MOB_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ARMORED_MOB_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return ARMORED_MOB_CAPABILITY.getStorage().writeNBT(ARMORED_MOB_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ARMORED_MOB_CAPABILITY.getStorage().readNBT(ARMORED_MOB_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
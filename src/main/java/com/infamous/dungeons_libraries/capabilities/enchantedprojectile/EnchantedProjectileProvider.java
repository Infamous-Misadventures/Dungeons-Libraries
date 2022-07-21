package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnchantedProjectileProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(EnchantedProjectile.class)
    public static final Capability<EnchantedProjectile> ENCHANTED_PROJECTILE_CAPABILITY = null;

    private LazyOptional<EnchantedProjectile> instance = LazyOptional.of(ENCHANTED_PROJECTILE_CAPABILITY::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ENCHANTED_PROJECTILE_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return ENCHANTED_PROJECTILE_CAPABILITY.getStorage().writeNBT(ENCHANTED_PROJECTILE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        ENCHANTED_PROJECTILE_CAPABILITY.getStorage().readNBT(ENCHANTED_PROJECTILE_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
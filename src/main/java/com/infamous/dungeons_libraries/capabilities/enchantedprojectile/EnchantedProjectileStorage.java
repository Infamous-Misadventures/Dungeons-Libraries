package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class EnchantedProjectileStorage implements Capability.IStorage<EnchantedProjectile> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<EnchantedProjectile> capability, EnchantedProjectile instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<EnchantedProjectile> capability, EnchantedProjectile instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.deserializeNBT(tag);
    }
}

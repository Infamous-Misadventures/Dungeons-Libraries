package com.infamous.dungeons_libraries.capabilities.armored;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ArmoredMobStorage implements Capability.IStorage<ArmoredMob> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ArmoredMob> capability, ArmoredMob instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<ArmoredMob> capability, ArmoredMob instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.deserializeNBT(tag);
    }
}

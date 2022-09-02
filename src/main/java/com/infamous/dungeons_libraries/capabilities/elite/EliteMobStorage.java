package com.infamous.dungeons_libraries.capabilities.elite;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class EliteMobStorage implements Capability.IStorage<EliteMob> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<EliteMob> capability, EliteMob instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<EliteMob> capability, EliteMob instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.deserializeNBT(tag);
    }
}

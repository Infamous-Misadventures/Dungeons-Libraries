package com.infamous.dungeons_libraries.capabilities.soulcaster;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.DoubleNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SoulCasterStorage implements Capability.IStorage<ISoulCaster> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<ISoulCaster> capability, ISoulCaster instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("souls", instance.getSouls());
        return tag;
    }

    @Override
    public void readNBT(Capability<ISoulCaster> capability, ISoulCaster instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        instance.setSouls(tag.getFloat("souls"), null);
    }
}

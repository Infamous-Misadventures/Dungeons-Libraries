package com.infamous.dungeons_libraries.capabilities.summoning;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MinionStorage implements Capability.IStorage<IMinion> {

    public static final String MASTER_KEY = "summoner";

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMinion> capability, IMinion instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        if(instance.getMaster() != null){
            tag.putUUID(MASTER_KEY, instance.getMaster());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IMinion> capability, IMinion instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        if(tag.hasUUID(MASTER_KEY)){
            instance.setMaster(tag.getUUID(MASTER_KEY));
        }
    }
}

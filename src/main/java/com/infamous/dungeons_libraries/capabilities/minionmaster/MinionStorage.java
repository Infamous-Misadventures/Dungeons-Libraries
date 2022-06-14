package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MinionStorage implements Capability.IStorage<IMinion> {

    public static final String MASTER_KEY = "summoner";
    public static final String LEVEL_KEY = "level";
    public static final String SUMMON_FLAG_KEY = "is_summon";

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMinion> capability, IMinion instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        if(instance.getMaster() != null){
            tag.putUUID(MASTER_KEY, instance.getMaster().getUUID());
            ResourceLocation location = instance.getMaster().level.dimension().location();
            tag.putString(LEVEL_KEY, location.toString());
        }
        tag.putBoolean(SUMMON_FLAG_KEY, instance.isSummon());
        return tag;
    }

    @Override
    public void readNBT(Capability<IMinion> capability, IMinion instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        if(tag.hasUUID(MASTER_KEY)){
            instance.setMasterUUID(tag.getUUID(MASTER_KEY));
        }
        if(tag.hasUUID(LEVEL_KEY)) {
            instance.setLevelOnLoad(new ResourceLocation(tag.getString(LEVEL_KEY)));
        }
        if(tag.contains(SUMMON_FLAG_KEY)){
            instance.setSummon(tag.getBoolean(SUMMON_FLAG_KEY));
        }
    }
}

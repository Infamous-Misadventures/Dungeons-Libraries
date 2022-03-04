package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MasterStorage implements Capability.IStorage<IMaster> {

    @Override
    public INBT writeNBT(Capability<IMaster> capability, IMaster instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        if(instance.getSummonedGolem() != null){
            tag.putUUID("golem", instance.getSummonedGolem());
        }
        if(instance.getSummonedWolf() != null){
            tag.putUUID("wolf", instance.getSummonedWolf());
        }
        if(instance.getSummonedLlama() != null){
            tag.putUUID("llama", instance.getSummonedLlama());
        }
        if(instance.getSummonedBat() != null){
            tag.putUUID("bat", instance.getSummonedBat());
        }
        if(instance.getSummonedSheep() != null){
            tag.putUUID("sheep", instance.getSummonedSheep());
        }
        ListNBT listnbt = new ListNBT();
        instance.getSummonedMobs().forEach(uuid -> {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putUUID("uuid", uuid);
            listnbt.add(compoundnbt);
        });
        tag.put("summoned", listnbt);
        return tag;
    }

    @Override
    public void readNBT(Capability<IMaster> capability, IMaster instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        if(tag.hasUUID("golem")){
            instance.setSummonedGolem(tag.getUUID("golem"));
        }
        if(tag.hasUUID("wolf")){
            instance.setSummonedWolf(tag.getUUID("wolf"));
        }
        if(tag.hasUUID("llama")){
            instance.setSummonedLlama(tag.getUUID("llama"));
        }
        if(tag.hasUUID("bat")){
            instance.setSummonedBat(tag.getUUID("bat"));
        }
        if(tag.hasUUID("sheep")){
            instance.setSummonedSheep(tag.getUUID("sheep"));
        }
        ListNBT listNBT = tag.getList("summoned", 10);
        List<UUID> summoned = new ArrayList<>();
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            summoned.add(compoundnbt.getUUID("uuid"));
        }
        instance.setSummonedMobs(summoned);
    }
}

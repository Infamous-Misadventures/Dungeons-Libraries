package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MasterStorage implements Capability.IStorage<IMaster> {

    public static final String LEVEL_KEY = "level";

    @Override
    public INBT writeNBT(Capability<IMaster> capability, IMaster instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        ListNBT listnbt = new ListNBT();
        instance.getSummonedMobs().forEach(entity -> {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putUUID("uuid", entity.getUUID());
            listnbt.add(compoundnbt);
        });
        tag.put("summoned", listnbt);
        if(!instance.getSummonedMobs().isEmpty()){
            ResourceLocation location = instance.getSummonedMobs().get(0).level.dimension().location();
            tag.putString(LEVEL_KEY, location.toString());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<IMaster> capability, IMaster instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        ListNBT listNBT = tag.getList("summoned", 10);
        List<UUID> summonedUUIDs = new ArrayList<>();
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            summonedUUIDs.add(compoundnbt.getUUID("uuid"));
        }
        instance.setSummonedMobsUUID(summonedUUIDs);
        if(tag.hasUUID(LEVEL_KEY)) {
            instance.setLevelOnLoad(new ResourceLocation(tag.getString(LEVEL_KEY)));
        }
    }
}

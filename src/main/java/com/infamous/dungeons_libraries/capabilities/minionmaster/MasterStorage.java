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
        CompoundNBT nbt = new CompoundNBT();
        ListNBT summoned = new ListNBT();
        instance.getSummonedMobs().forEach(entity -> {
            CompoundNBT mob = new CompoundNBT();
            mob.putUUID("uuid", entity.getUUID());
            summoned.add(mob);
        });
        nbt.put("summoned", summoned);
        ListNBT minions = new ListNBT();
        instance.getOtherMinions().forEach(entity -> {
            CompoundNBT minion = new CompoundNBT();
            minion.putUUID("uuid", entity.getUUID());
            minions.add(minion);
        });
        nbt.put("minions", minions);
        if(!instance.getSummonedMobs().isEmpty()){
            ResourceLocation location = instance.getSummonedMobs().get(0).level.dimension().location();
            nbt.putString(LEVEL_KEY, location.toString());
        }else if(!instance.getOtherMinions().isEmpty()){
            ResourceLocation location = instance.getOtherMinions().get(0).level.dimension().location();
            nbt.putString(LEVEL_KEY, location.toString());
        }
        return nbt;
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
        ListNBT minionsNBT = tag.getList("minions", 10);
        List<UUID> minionUUIDs = new ArrayList<>();
        for(int i = 0; i < minionsNBT.size(); ++i) {
            CompoundNBT compoundnbt = minionsNBT.getCompound(i);
            minionUUIDs.add(compoundnbt.getUUID("uuid"));
        }
        if(tag.contains(LEVEL_KEY)) {
            instance.setLevelOnLoad(new ResourceLocation(tag.getString(LEVEL_KEY)));
        }
    }
}

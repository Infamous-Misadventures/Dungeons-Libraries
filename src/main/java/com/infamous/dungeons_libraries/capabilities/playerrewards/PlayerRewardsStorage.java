package com.infamous.dungeons_libraries.capabilities.playerrewards;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;


public class PlayerRewardsStorage implements Capability.IStorage<IPlayerRewards> {

    @Override
    public INBT writeNBT(Capability<IPlayerRewards> capability, IPlayerRewards instance, Direction side) {
        CompoundNBT nbt = new CompoundNBT();
        Map<ResourceLocation, Integer> rewards = instance.getAllPlayerRewards();
        ListNBT listNBT = new ListNBT();
        for(Map.Entry<ResourceLocation, Integer> entry : rewards.entrySet()){
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putString("source", entry.getKey().toString());
            compoundNBT.putInt("amount", entry.getValue());
            listNBT.add(compoundNBT);
        }
        nbt.put("rewards", listNBT);
        return nbt;
    }

    @Override
    public void readNBT(Capability<IPlayerRewards> capability, IPlayerRewards instance, Direction side, INBT nbt) {
        if(nbt instanceof CompoundNBT){
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            ListNBT listNBT = compoundNBT.getList("rewards", 10);
            for(INBT inbt : listNBT){
                if(inbt instanceof CompoundNBT){
                    CompoundNBT compoundNBT1 = (CompoundNBT) inbt;
                    ResourceLocation resourceLocation = new ResourceLocation(compoundNBT1.getString("source"));
                    Integer amount = compoundNBT1.getInt("amount");
                    instance.setPlayerRewards(resourceLocation, amount);
                }
            }
        }
    }
}
package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.util.NonNullList;

import java.util.List;
import java.util.UUID;

public interface IMaster {

    void copyFrom(IMaster summoner);

    void setSummonedGolem(UUID golem);
    void setSummonedWolf(UUID wolf);
    void setSummonedLlama(UUID llama);
    void setSummonedBat(UUID bat);
    void setSummonedSheep(UUID enchantedSheep);

    UUID getSummonedGolem();
    UUID getSummonedWolf();
    UUID getSummonedLlama();
    UUID getSummonedBat();
    UUID getSummonedSheep();

    default List<UUID> getSummonedMobs(){
        List<UUID> summonedMobs = NonNullList.create();
        if(this.getSummonedBat() != null){
            summonedMobs.add(this.getSummonedBat());
        }
        if(this.getSummonedGolem() != null){
            summonedMobs.add(this.getSummonedGolem());
        }
        if(this.getSummonedLlama() != null){
            summonedMobs.add(this.getSummonedLlama());
        }
        if(this.getSummonedSheep() != null){
            summonedMobs.add(this.getSummonedSheep());
        }
        if(this.getSummonedWolf() != null){
            summonedMobs.add(this.getSummonedWolf());
        }
        return summonedMobs;
    }

    boolean addSummonedMob(UUID uuid);

    void setSummonedMobs(List<UUID> summonedMobs);
}

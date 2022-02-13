package com.infamous.dungeons_libraries.capabilities.minionmaster;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Master implements IMaster {

    @Nullable
    private UUID summonedGolem;
    @Nullable
    private UUID summonedWolf;
    @Nullable
    private UUID summonedLlama;
    @Nullable
    private UUID summonedBat;
    @Nullable
    private UUID summonedSheep;

    private UUID[] buzzyNestBees = new UUID[3];
    private UUID[] busyBeeBees = new UUID[3];
    private UUID[] tumblebeeBees = new UUID[3];

    private List<UUID> summonedMobs = new ArrayList<>();

    @Override
    public void copyFrom(IMaster summoner) {
        this.setSummonedBat(summoner.getSummonedBat());
        this.setSummonedGolem(summoner.getSummonedGolem());
        this.setSummonedLlama(summoner.getSummonedLlama());
        this.setSummonedSheep(summoner.getSummonedSheep());
        this.setSummonedWolf(summoner.getSummonedBat());
    }

    @Override
    public void setSummonedGolem(@Nullable UUID golem) {
        this.summonedGolem = golem;
    }

    @Override
    public void setSummonedWolf(@Nullable UUID wolf) {
        this.summonedWolf = wolf;
    }

    @Override
    public void setSummonedLlama(@Nullable UUID llama) {
        this.summonedLlama = llama;
    }

    @Override
    public void setSummonedBat(@Nullable UUID bat) {
        this.summonedBat = bat;
    }

    @Override
    public void setSummonedSheep(UUID enchantedSheep) {
        this.summonedSheep = enchantedSheep;
    }

    @Override
    @Nullable
    public UUID getSummonedGolem() {
        return this.summonedGolem;
    }

    @Override
    @Nullable
    public UUID getSummonedWolf() {
        return this.summonedWolf;
    }

    @Override
    @Nullable
    public UUID getSummonedLlama() {
        return this.summonedLlama;
    }

    @Override
    @Nullable
    public UUID getSummonedBat() {
        return this.summonedBat;
    }

    @Override
    public UUID getSummonedSheep() {
        return this.summonedSheep;
    }

    @Override
    public List<UUID> getSummonedMobs() {
        List<UUID> summonedMobsTotal = IMaster.super.getSummonedMobs();
        summonedMobsTotal.addAll(this.summonedMobs);
        return summonedMobsTotal;
    }

    @Override
    public boolean addSummonedMob(UUID uuid) {
        return summonedMobs.add(uuid);
    }

    @Override
    public void setSummonedMobs(List<UUID> summonedMobs) {
        this.summonedMobs = new ArrayList<>(summonedMobs);
    }
}

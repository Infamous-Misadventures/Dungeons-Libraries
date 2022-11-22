package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.summon.SummonConfigRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;
import java.util.stream.Collectors;

public class Master implements IMaster {

    private Set<Entity> summonedMobs;
    private List<UUID> summonedMobsUUID = new ArrayList<>();
    private ResourceLocation levelOnLoad;
    private Set<Entity> otherMinions;
    private List<UUID> otherMinionsUUID = new ArrayList<>();

    @Override
    public void copyFrom(IMaster summoner) {
        this.setSummonedMobs(summoner.getSummonedMobs());
    }

    @Override
    public List<Entity> getAllMinions() {
        List<Entity> minions = new ArrayList<>();
        minions.addAll(this.getSummonedMobs());
        minions.addAll(this.getOtherMinions());
        return minions;
    }

    @Override
    public List<Entity> getSummonedMobs() {
        summonedMobs = initEntities(this.summonedMobs, this.summonedMobsUUID);
        return new ArrayList<>(this.summonedMobs);
    }

    @Override
    public int getSummonedMobsCost(){
        return this.getSummonedMobs().stream().map(entity -> SummonConfigRegistry.getConfig(entity.getType().getRegistryName()).getCost()).reduce(0, Integer::sum);
    }

    @Override
    public boolean addSummonedMob(Entity entity) {
        summonedMobs = initEntities(this.summonedMobs, this.summonedMobsUUID);
        return this.summonedMobs.add(entity);
    }

    @Override
    public void setSummonedMobs(List<Entity> summonedMobs) {
        this.summonedMobs = new HashSet<>(summonedMobs);
    }

    @Override
    public void setSummonedMobsUUID(List<UUID> summonedMobsUUID) {
        this.summonedMobsUUID = summonedMobsUUID;
    }

    @Override
    public void setLevelOnLoad(ResourceLocation levelOnLoad) {
        this.levelOnLoad = levelOnLoad;
    }

    @Override
    public boolean addMinion(Entity entity) {
        otherMinions = initEntities(this.otherMinions, this.otherMinionsUUID);
        return otherMinions.add(entity);
    }

    @Override
    public List<Entity> getOtherMinions() {
        otherMinions = initEntities(this.otherMinions, this.otherMinionsUUID);
        return new ArrayList<>(this.otherMinions);
    }

    @Override
    public void setOtherMinions(List<Entity> otherMinions) {
        this.otherMinions = new HashSet<>(otherMinions);
    }

    private Set<Entity> initEntities(Set<Entity> entities, List<UUID> entityUUIDs) {
        if(entities != null) return entities;
        if(entityUUIDs != null && this.levelOnLoad != null){
            if(entityUUIDs.isEmpty()) return new HashSet<>();
            RegistryKey<World> registrykey1 = RegistryKey.create(Registry.DIMENSION_REGISTRY, this.levelOnLoad);
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerWorld world = server.getLevel(registrykey1);
            if (world != null) {
                entities = entityUUIDs.stream().map(world::getEntity).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        }else{
           return new HashSet<>();
        }
        return entities;
    }

    @Override
    public void removeMinion(LivingEntity entityLiving) {
        this.getOtherMinions().remove(entityLiving);
    }
}

package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.summon.SummonConfigRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Master implements IMaster {

    private List<Entity> summonedMobs;
    private List<UUID> summonedMobsUUID = new ArrayList<>();
    private ResourceLocation levelOnLoad;
    private List<Entity> otherMinions;
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
        return getEntities(this.summonedMobs, this.summonedMobsUUID);
    }

    @Override
    public int getSummonedMobsCost(){
        return this.getSummonedMobs().stream().map(entity -> SummonConfigRegistry.getConfig(entity.getType().getRegistryName()).getCost()).reduce(0, Integer::sum);
    }

    @Override
    public boolean addSummonedMob(Entity entity) {
        return this.getSummonedMobs().add(entity);
    }

    @Override
    public void setSummonedMobs(List<Entity> summonedMobs) {
        this.summonedMobs = new ArrayList<>(summonedMobs);
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
        return this.getOtherMinions().add(entity);
    }

    @Override
    public List<Entity> getOtherMinions() {
        return getEntities(this.otherMinions, this.otherMinionsUUID);
    }

    @Override
    public void setOtherMinions(List<Entity> otherMinions) {
        this.otherMinions = new ArrayList<>(otherMinions);
    }

    private List<Entity> getEntities(List<Entity> entities, List<UUID> entityUUIDs) {
        if(entities != null) return entities;
        if(entityUUIDs != null && this.levelOnLoad != null){
            if(entityUUIDs.isEmpty()) return new ArrayList<>();
            RegistryKey<World> registrykey1 = RegistryKey.create(Registry.DIMENSION_REGISTRY, this.levelOnLoad);
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerWorld world = server.getLevel(registrykey1);
            if (world != null) {
                entities = entityUUIDs.stream().map(world::getEntity).filter(Objects::nonNull).collect(Collectors.toList());
            }
        }else{
            entities = new ArrayList<>();
        }
        return entities;
    }


}

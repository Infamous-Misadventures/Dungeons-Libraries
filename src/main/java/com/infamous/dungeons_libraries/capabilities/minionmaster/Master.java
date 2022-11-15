package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.summon.SummonConfigRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.stream.Collectors;

public class Master implements INBTSerializable<CompoundTag> {

    private Set<Entity> summonedMobs;
    private List<UUID> summonedMobsUUID = new ArrayList<>();
    private ResourceLocation levelOnLoad;
    private Set<Entity> otherMinions;
    private List<UUID> otherMinionsUUID = new ArrayList<>();

    public void copyFrom(Master summoner) {
        this.setSummonedMobs(summoner.getSummonedMobs());
    }

    public List<Entity> getAllMinions() {
        List<Entity> minions = new ArrayList<>();
        minions.addAll(this.getSummonedMobs());
        minions.addAll(this.getOtherMinions());
        return minions;
    }

    public List<Entity> getSummonedMobs() {
        return getEntities(this.summonedMobs, this.summonedMobsUUID);
    }

    public int getSummonedMobsCost(){
        return this.getSummonedMobs().stream().map(entity -> SummonConfigRegistry.getConfig(entity.getType().getRegistryName()).getCost()).reduce(0, Integer::sum);
    }

    public boolean addSummonedMob(Entity entity) {
        this.getSummonedMobs();
        return summonedMobs.add(entity);
    }

    public void setSummonedMobs(List<Entity> summonedMobs) {
        this.summonedMobs = new HashSet<>(summonedMobs);
    }

    public void setSummonedMobsUUID(List<UUID> summonedMobsUUID) {
        this.summonedMobsUUID = summonedMobsUUID;
    }

    public void setLevelOnLoad(ResourceLocation levelOnLoad) {
        this.levelOnLoad = levelOnLoad;
    }

    public boolean addMinion(Entity entity) {
        this.getOtherMinions();
        return otherMinions.add(entity);
    }

    public List<Entity> getOtherMinions() {
        return getEntities(this.otherMinions, this.otherMinionsUUID);
    }

    public void setOtherMinions(List<Entity> otherMinions) {
        this.otherMinions = new HashSet<>(otherMinions);
    }

    private List<Entity> getEntities(Set<Entity> entities, List<UUID> entityUUIDs) {
        if(entities != null) return new ArrayList<>(entities);
        if(entityUUIDs != null && this.levelOnLoad != null){
            if(entityUUIDs.isEmpty()) return new ArrayList<>();
            ResourceKey<Level> registrykey1 = ResourceKey.create(Registry.DIMENSION_REGISTRY, this.levelOnLoad);
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(registrykey1);
            if (world != null) {
                entities = entityUUIDs.stream().map(world::getEntity).filter(Objects::nonNull).collect(Collectors.toSet());
            }
        }else{
            return new ArrayList<>();
        }
        return new ArrayList<>(entities);
    }

    public void removeMinion(LivingEntity entityLiving) {
        this.getOtherMinions().remove(entityLiving);
    }

    public static final String LEVEL_KEY = "level";

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag summoned = new ListTag();
        this.getSummonedMobs().forEach(entity -> {
            CompoundTag mob = new CompoundTag();
            mob.putUUID("uuid", entity.getUUID());
            summoned.add(mob);
        });
        nbt.put("summoned", summoned);
        ListTag minions = new ListTag();
        this.getOtherMinions().forEach(entity -> {
            CompoundTag minion = new CompoundTag();
            minion.putUUID("uuid", entity.getUUID());
            minions.add(minion);
        });
        nbt.put("minions", minions);
        if(!this.getSummonedMobs().isEmpty()){
            ResourceLocation location = this.getSummonedMobs().get(0).level.dimension().location();
            nbt.putString(LEVEL_KEY, location.toString());
        }else if(!this.getOtherMinions().isEmpty()){
            ResourceLocation location = this.getOtherMinions().get(0).level.dimension().location();
            nbt.putString(LEVEL_KEY, location.toString());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ListTag listNBT = tag.getList("summoned", 10);
        List<UUID> summonedUUIDs = new ArrayList<>();
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundTag compoundnbt = listNBT.getCompound(i);
            summonedUUIDs.add(compoundnbt.getUUID("uuid"));
        }
        this.setSummonedMobsUUID(summonedUUIDs);
        ListTag minionsNBT = tag.getList("minions", 10);
        List<UUID> minionUUIDs = new ArrayList<>();
        for(int i = 0; i < minionsNBT.size(); ++i) {
            CompoundTag compoundnbt = minionsNBT.getCompound(i);
            minionUUIDs.add(compoundnbt.getUUID("uuid"));
        }
        if(tag.contains(LEVEL_KEY)) {
            this.setLevelOnLoad(new ResourceLocation(tag.getString(LEVEL_KEY)));
        }
    }
}

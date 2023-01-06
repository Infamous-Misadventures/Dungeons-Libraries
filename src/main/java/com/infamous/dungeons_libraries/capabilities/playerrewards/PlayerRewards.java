package com.infamous.dungeons_libraries.capabilities.playerrewards;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class PlayerRewards implements INBTSerializable<CompoundTag> {
    private Map<ResourceLocation, Integer> rewards = new HashMap<>();

    public PlayerRewards() {
    }

    public boolean addPlayerReward(ResourceLocation source) {
        rewards.put(source, rewards.computeIfAbsent(source, resourceLocation -> 1) + 1);
        return true;
    }

    public boolean removePlayerReward(ResourceLocation source) {
        if (!rewards.containsKey(source) || rewards.get(source) == 0) {
            return true;
        }
        rewards.put(source, rewards.get(source) - 1);
        return true;
    }

    public boolean setPlayerRewards(ResourceLocation source, Integer amount) {
        rewards.put(source, amount);
        return true;
    }

    public Integer getPlayerRewardAmount(ResourceLocation source) {
        return rewards.getOrDefault(source, 0);
    }

    public boolean hasPlayerReward(ResourceLocation source) {
        return rewards.containsKey(source) && rewards.get(source) > 0;
    }

    public Map<ResourceLocation, Integer> getAllPlayerRewards() {
        return new HashMap<>(rewards);
    }

    public boolean setPlayerRewards(Map<ResourceLocation, Integer> allPlayerRewards) {
        rewards = new HashMap<>(allPlayerRewards);
        return true;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        Map<ResourceLocation, Integer> rewards = this.getAllPlayerRewards();
        ListTag listNBT = new ListTag();
        for (Map.Entry<ResourceLocation, Integer> entry : rewards.entrySet()) {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putString("source", entry.getKey().toString());
            compoundNBT.putInt("amount", entry.getValue());
            listNBT.add(compoundNBT);
        }
        nbt.put("rewards", listNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        ListTag listNBT = tag.getList("rewards", 10);
        for (Tag inbt : listNBT) {
            if (inbt instanceof CompoundTag) {
                CompoundTag compoundNBT1 = (CompoundTag) inbt;
                ResourceLocation resourceLocation = new ResourceLocation(compoundNBT1.getString("source"));
                Integer amount = compoundNBT1.getInt("amount");
                this.setPlayerRewards(resourceLocation, amount);
            }
        }
    }
}

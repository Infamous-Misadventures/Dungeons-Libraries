package com.infamous.dungeons_libraries.capabilities.playerrewards;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class PlayerRewards implements IPlayerRewards {
    private Map<ResourceLocation, Integer> rewards = new HashMap<>();

    public PlayerRewards() {
    }

    @Override
    public boolean addPlayerReward(ResourceLocation source) {
        rewards.put(source, rewards.computeIfAbsent(source, resourceLocation -> 1) + 1);
        return true;
    }

    @Override
    public boolean removePlayerReward(ResourceLocation source) {
        if(!rewards.containsKey(source) || rewards.get(source) == 0){
            return true;
        }
        rewards.put(source, rewards.get(source) - 1);
        return true;
    }

    @Override
    public boolean setPlayerRewards(ResourceLocation source, Integer amount) {
        rewards.put(source, amount);
        return true;
    }

    @Override
    public Integer getPlayerRewardAmount(ResourceLocation source) {
        return rewards.getOrDefault(source, 0);
    }

    @Override
    public boolean hasPlayerReward(ResourceLocation source) {
        return rewards.containsKey(source) && rewards.get(source) > 0;
    }

    @Override
    public Map<ResourceLocation, Integer> getAllPlayerRewards() {
        return new HashMap<>(rewards);
    }
}

package com.infamous.dungeons_libraries.capabilities.playerrewards;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface IPlayerRewards {

    boolean addPlayerReward(ResourceLocation source);

    boolean removePlayerReward(ResourceLocation source);

    boolean setPlayerRewards(ResourceLocation source, Integer amount);

    Integer getPlayerRewardAmount(ResourceLocation source);

    boolean hasPlayerReward(ResourceLocation source);

    Map<ResourceLocation, Integer> getAllPlayerRewards();

    boolean setPlayerRewards(Map<ResourceLocation, Integer> allPlayerRewards);
}

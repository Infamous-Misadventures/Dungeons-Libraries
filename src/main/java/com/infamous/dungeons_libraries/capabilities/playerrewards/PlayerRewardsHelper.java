package com.infamous.dungeons_libraries.capabilities.playerrewards;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.PLAYER_REWARDS_CAPABILITY;


public class PlayerRewardsHelper {
    public static LazyOptional<PlayerRewards> getPlayerRewardsCapabilityLazy(ItemStack itemStack)
    {
        if(PLAYER_REWARDS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<PlayerRewards> lazyCap = itemStack.getCapability(PLAYER_REWARDS_CAPABILITY);
        return lazyCap;
    }

    public static PlayerRewards getPlayerRewardsCapability(Player playerEntity)
    {
        LazyOptional<PlayerRewards> lazyCap = playerEntity.getCapability(PLAYER_REWARDS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Player Rewards capability from the Player!"));
        }
        return null;
    }

}

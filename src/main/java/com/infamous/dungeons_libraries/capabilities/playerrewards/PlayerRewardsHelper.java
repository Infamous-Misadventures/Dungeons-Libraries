package com.infamous.dungeons_libraries.capabilities.playerrewards;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY;

public class PlayerRewardsHelper {
    public static LazyOptional<IPlayerRewards> getPlayerRewardsCapabilityLazy(ItemStack itemStack)
    {
        if(BUILT_IN_ENCHANTMENTS_CAPABILITY == null) {
            return LazyOptional.empty();
        }
        LazyOptional<IPlayerRewards> lazyCap = itemStack.getCapability(PlayerRewardsProvider.PLAYER_REWARDS_CAPABILITY);
        return lazyCap;
    }

    public static IPlayerRewards getPlayerRewardsCapability(PlayerEntity playerEntity)
    {
        LazyOptional<IPlayerRewards> lazyCap = playerEntity.getCapability(PlayerRewardsProvider.PLAYER_REWARDS_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the Player Rewards capability from the Player!"));
        }
        return null;
    }

}

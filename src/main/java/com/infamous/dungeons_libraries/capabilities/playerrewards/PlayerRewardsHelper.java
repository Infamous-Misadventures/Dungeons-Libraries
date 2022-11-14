package com.infamous.dungeons_libraries.capabilities.playerrewards;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider.BUILT_IN_ENCHANTMENTS_CAPABILITY;

public class PlayerRewardsHelper {

    public static IPlayerRewards getPlayerRewardsCapability(PlayerEntity playerEntity)
    {
        LazyOptional<IPlayerRewards> lazyCap = playerEntity.getCapability(PlayerRewardsProvider.PLAYER_REWARDS_CAPABILITY);
        return lazyCap.orElse(new PlayerRewards());
    }

}

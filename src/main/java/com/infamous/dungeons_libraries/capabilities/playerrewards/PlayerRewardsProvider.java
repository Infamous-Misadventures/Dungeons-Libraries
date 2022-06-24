package com.infamous.dungeons_libraries.capabilities.playerrewards;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerRewardsProvider implements ICapabilitySerializable<INBT> {

        @CapabilityInject(IPlayerRewards.class)
        public static final Capability<IPlayerRewards> PLAYER_REWARDS_CAPABILITY = null;

        private LazyOptional<IPlayerRewards> instance = LazyOptional.of(PLAYER_REWARDS_CAPABILITY::getDefaultInstance);

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == PLAYER_REWARDS_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

        @Override
        public INBT serializeNBT() {
            return PLAYER_REWARDS_CAPABILITY.getStorage().writeNBT(PLAYER_REWARDS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
        }

        @Override
        public void deserializeNBT(INBT nbt) {
            PLAYER_REWARDS_CAPABILITY.getStorage().readNBT(PLAYER_REWARDS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
        }
}
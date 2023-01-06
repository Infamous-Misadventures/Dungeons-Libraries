package com.infamous.dungeons_libraries.capabilities.playerrewards;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class AttacherPlayerRewards {

    private static class PlayerRewardsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(MODID, "player_rewards");
        private final PlayerRewards backend = new PlayerRewards();
        private final LazyOptional<PlayerRewards> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return ModCapabilities.PLAYER_REWARDS_CAPABILITY.orEmpty(cap, this.optionalData);
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.backend.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.backend.deserializeNBT(nbt);
        }
    }

    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayer) {
            final AttacherPlayerRewards.PlayerRewardsProvider provider = new AttacherPlayerRewards.PlayerRewardsProvider();
            event.addCapability(AttacherPlayerRewards.PlayerRewardsProvider.IDENTIFIER, provider);
        }
    }
}

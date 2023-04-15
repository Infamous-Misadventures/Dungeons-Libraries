package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class AttacherLeader {

    private static class LeaderProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(MODID, "master");
        private final Leader backend = new Leader();
        private final LazyOptional<Leader> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return ModCapabilities.LEADER_CAPABILITY.orEmpty(cap, this.optionalData);
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

    // attach only to living entities
    public static void attach(final AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            final LeaderProvider provider = new LeaderProvider();
            event.addCapability(LeaderProvider.IDENTIFIER, provider);
        }
    }
}

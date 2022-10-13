package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class AttacherEnchantedProjectile {

    private static class EnchantedProjectileProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(MODID, "enchanted_projectile");
        private final EnchantedProjectile backend = new EnchantedProjectile();
        private final LazyOptional<EnchantedProjectile> optionalData = LazyOptional.of(() -> backend);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return ModCapabilities.ENCHANTED_PROJECTILE_CAPABILITY.orEmpty(cap, this.optionalData);
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
        if(event.getObject() instanceof Projectile) {
            final AttacherEnchantedProjectile.EnchantedProjectileProvider provider = new AttacherEnchantedProjectile.EnchantedProjectileProvider();
            event.addCapability(AttacherEnchantedProjectile.EnchantedProjectileProvider.IDENTIFIER, provider);
        }
    }
}

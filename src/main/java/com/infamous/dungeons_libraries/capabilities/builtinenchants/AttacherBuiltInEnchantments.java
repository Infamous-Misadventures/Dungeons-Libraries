package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import org.jetbrains.annotations.NotNull;

import com.infamous.dungeons_libraries.capabilities.ModCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class AttacherBuiltInEnchantments {

    private static class BuiltInEnchantmentsProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

        public static final ResourceLocation IDENTIFIER = new ResourceLocation(MODID, "built_in_enchantments");
        private final BuiltInEnchantments backend;
        private final LazyOptional<BuiltInEnchantments> optionalData;

        public BuiltInEnchantmentsProvider(ItemStack itemStack) {
            backend = new BuiltInEnchantments(itemStack);
            optionalData = LazyOptional.of(() -> backend);
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return ModCapabilities.BUILT_IN_ENCHANTMENTS_CAPABILITY.orEmpty(cap, this.optionalData);
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

    public static void attach(final AttachCapabilitiesEvent<ItemStack> event) {
        final AttacherBuiltInEnchantments.BuiltInEnchantmentsProvider provider = new AttacherBuiltInEnchantments.BuiltInEnchantmentsProvider(event.getObject());

        if (event.getObject().isEnchantable() && event.getObject().getMaxStackSize() == 1) {
            event.addCapability(AttacherBuiltInEnchantments.BuiltInEnchantmentsProvider.IDENTIFIER, provider);
        }
    }
}

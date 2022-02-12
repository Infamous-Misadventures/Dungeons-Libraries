package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BuiltInEnchantmentsProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IBuiltInEnchantments.class)
    public static final Capability<IBuiltInEnchantments> BUILT_IN_ENCHANTMENTS_CAPABILITY = null;

    private LazyOptional<IBuiltInEnchantments> instance = LazyOptional.of(BUILT_IN_ENCHANTMENTS_CAPABILITY::getDefaultInstance);

    public BuiltInEnchantmentsProvider(ItemStack itemStack) {
        this.instance = LazyOptional.of(() -> new BuiltInEnchantments(itemStack));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == BUILT_IN_ENCHANTMENTS_CAPABILITY ? instance.cast() : LazyOptional.empty();    }

    @Override
    public INBT serializeNBT() {
        return BUILT_IN_ENCHANTMENTS_CAPABILITY.getStorage().writeNBT(BUILT_IN_ENCHANTMENTS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        BUILT_IN_ENCHANTMENTS_CAPABILITY.getStorage().readNBT(BUILT_IN_ENCHANTMENTS_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
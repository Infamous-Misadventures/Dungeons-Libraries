package com.infamous.dungeons_libraries.capabilities.timers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public class TimersStorage implements Capability.IStorage<ITimers> {

    public static final String ENCHANTS_KEY = "EnchantmentTimers";
    public static final String ENCHANTMENT_KEY = "Enchantment";
    public static final String TIMER_KEY = "Timer";

    @Override
    public INBT writeNBT(Capability<ITimers> capability, ITimers instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        ListNBT listnbt = new ListNBT();
        instance.getEnchantmentTimers().forEach((resourceLocation, timer) -> {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putString(ENCHANTMENT_KEY, resourceLocation.toString());
            compoundnbt.putInt(TIMER_KEY, timer);
            listnbt.add(compoundnbt);
        });
        tag.put(ENCHANTS_KEY, listnbt);
        return tag;
    }

    @Override
    public void readNBT(Capability<ITimers> capability, ITimers instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        ListNBT listNBT = tag.getList(ENCHANTS_KEY, 10);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            ResourceLocation resourcelocation = ResourceLocation.tryParse(compoundnbt.getString(ENCHANTMENT_KEY));
            int timer = compoundnbt.getInt(TIMER_KEY);
            instance.setEnchantmentTimer(resourcelocation, timer);
        }
    }
}
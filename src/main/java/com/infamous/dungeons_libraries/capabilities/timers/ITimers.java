package com.infamous.dungeons_libraries.capabilities.timers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface ITimers {

    int getEnchantmentTimer(Enchantment enchantment);

    boolean setEnchantmentTimer(Enchantment enchantment, int value);

    boolean setEnchantmentTimer(ResourceLocation enchantment, int value);

    boolean tickTimers();

    Map<ResourceLocation, Integer> getEnchantmentTimers();

}

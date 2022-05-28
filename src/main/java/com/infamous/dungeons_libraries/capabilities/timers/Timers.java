package com.infamous.dungeons_libraries.capabilities.timers;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Timers implements ITimers {
    private final Map<ResourceLocation, Integer> enchantmentTimers = new HashMap<>();

    @Override
    public int getEnchantmentTimer(Enchantment enchantment) {
        return enchantmentTimers.computeIfAbsent(enchantment.getRegistryName(), resourceLocation -> -1);
    }

    @Override
    public boolean setEnchantmentTimer(Enchantment enchantment, int value) {
        enchantmentTimers.put(enchantment.getRegistryName(), value);
        return true;
    }

    @Override
    public boolean setEnchantmentTimer(ResourceLocation enchantment, int value) {
        enchantmentTimers.put(enchantment, value);
        return true;
    }

    @Override
    public boolean tickTimers() {
        enchantmentTimers.replaceAll((resourceLocation, integer) -> integer > 0 ? integer - 1 : integer);
        return true;
    }

    @Override
    public Map<ResourceLocation, Integer> getEnchantmentTimers() {
        return enchantmentTimers;
    }
}

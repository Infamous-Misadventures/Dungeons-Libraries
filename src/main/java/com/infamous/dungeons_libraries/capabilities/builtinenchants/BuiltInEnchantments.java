package com.infamous.dungeons_libraries.capabilities.builtinenchants;


import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class BuiltInEnchantments implements IBuiltInEnchantments {
    private Map<ResourceLocation, List<EnchantmentData>> enchantments = new HashMap<>();

    @Override
    public boolean addBuiltInEnchantment(ResourceLocation source, EnchantmentData enchantmentData) {
        enchantments.computeIfAbsent(source, resourceLocation -> enchantments.put(resourceLocation, new ArrayList<>()));
        enchantments.get(source).add(enchantmentData);
        return true;
    }

    @Override
    public boolean removeBuiltInEnchantment(ResourceLocation source, Enchantment enchantment) {
        if(!enchantments.containsKey(source)){
            return false;
        }
        enchantments.put(source, enchantments.get(source).stream().filter(enchantmentData -> enchantmentData.enchantment != enchantment).collect(Collectors.toList()));
        return true;
    }

    @Override
    public boolean clearAllBuiltInEnchantments(ResourceLocation source) {
        enchantments.remove(source);
        return true;
    }

    @Override
    public List<EnchantmentData> getBuiltInEnchantments(ResourceLocation source) {
        List<EnchantmentData> enchantmentData = enchantments.get(source);
        if(enchantmentData == null){
            return Lists.newArrayList();
        }
        return enchantmentData;
    }

    @Override
    public List<EnchantmentData> getAllBuiltInEnchantmentDatas() {
        return enchantments.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Map<ResourceLocation, List<EnchantmentData>> getAllBuiltInEnchantmentDatasPerSource() {
        return enchantments;
    }

    @Override
    public boolean hasBuiltInEnchantment(ResourceLocation source) {
        return !getBuiltInEnchantments(source).isEmpty();
    }

    @Override
    public boolean hasBuiltInEnchantment() {
        return !this.enchantments.isEmpty();
    }

    @Override
    public boolean hasBuiltInEnchantment(Enchantment enchantment) {
        return getAllBuiltInEnchantmentDatas().stream().anyMatch(enchantmentData -> enchantmentData.enchantment.equals(enchantment));
    }

    @Override
    public int getBuiltInItemEnchantmentLevel(Enchantment enchantment) {
        return getAllBuiltInEnchantmentDatas().stream().filter(enchantmentData -> enchantmentData.enchantment.equals(enchantment)).map(enchantmentData -> enchantmentData.level).max(Comparator.naturalOrder()).orElse(0);
    }
}

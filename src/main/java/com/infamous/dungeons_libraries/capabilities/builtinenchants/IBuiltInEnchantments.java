package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface IBuiltInEnchantments {

    boolean addBuiltInEnchantment(ResourceLocation source, EnchantmentData enchantmentData);

    boolean removeBuiltInEnchantment(ResourceLocation source, Enchantment enchantment);

    boolean setBuiltInEnchantments(ResourceLocation source, List<EnchantmentData> enchantmentData);

    boolean clearAllBuiltInEnchantments(ResourceLocation source);

    List<EnchantmentData> getBuiltInEnchantments(ResourceLocation source);
    List<EnchantmentData> getAllBuiltInEnchantmentDatas();
    Map<ResourceLocation, List<EnchantmentData>> getAllBuiltInEnchantmentDatasPerSource();

    boolean hasBuiltInEnchantment(ResourceLocation source);
    boolean hasBuiltInEnchantment();

    boolean hasBuiltInEnchantment(Enchantment enchantment);
    int getBuiltInItemEnchantmentLevel(Enchantment enchantment);

}

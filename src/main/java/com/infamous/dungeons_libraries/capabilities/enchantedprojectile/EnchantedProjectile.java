package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnchantedProjectile {
    private List<EnchantmentInstance> enchantmentDataList = new ArrayList<>();

    public void setEnchantments(ItemStack itemStack){
        Map<Enchantment, Integer> itemStackEnchantmentInstance = EnchantmentHelper.getEnchantments(itemStack);
        LazyOptional<BuiltInEnchantments> lazyCap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapabilityLazy(itemStack);
        if(lazyCap.isPresent()){
            Map<Enchantment, Integer> builtInEnchantments = lazyCap.resolve().get().getAllBuiltInEnchantmentInstances().stream()
                .collect(Collectors.groupingBy(enchantmentData -> enchantmentData.enchantment, Collectors.summingInt(value -> value.level)));
            builtInEnchantments.forEach((enchantment, integer) -> itemStackEnchantmentInstance.compute(enchantment, (enchantment1, integer1) -> integer1 == null ? integer : integer1 + integer));
        }
        enchantmentDataList = itemStackEnchantmentInstance.entrySet().stream()
            .map(entry -> new EnchantmentInstance(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public int getEnchantmentLevel(Enchantment enchantment){
        return enchantmentDataList.stream()
            .filter(enchantmentData -> enchantmentData.enchantment == enchantment)
            .map(enchantmentData -> enchantmentData.level)
            .findFirst()
            .orElse(0);
    }

    public static final String ENCHANTMENT_DATA_KEY = "EnchantmentInstance";
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ListTag enchantmentListnbt = new ListTag();
        enchantmentDataList.forEach(enchantmentData -> {
            CompoundTag enchantmentDataNBT = new CompoundTag();
            enchantmentDataNBT.putString("id", String.valueOf(enchantmentData.enchantment.getRegistryName()));
            enchantmentDataNBT.putShort("lvl", (short)enchantmentData.level);
            enchantmentListnbt.add(enchantmentDataNBT);
        });
        nbt.put(ENCHANTMENT_DATA_KEY, enchantmentListnbt);
        return nbt;
    }

    public void deserializeNBT(CompoundTag tag) {
        Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.deserializeEnchantments(tag.getList(ENCHANTMENT_DATA_KEY, 10));
        enchantmentDataList = enchantmentIntegerMap.entrySet().stream().map(entry -> new EnchantmentInstance(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

}

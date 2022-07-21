package com.infamous.dungeons_libraries.capabilities.enchantedprojectile;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.LazyOptional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EnchantedProjectile {
    private List<EnchantmentData> enchantmentDataList = new ArrayList<>();

    public void setEnchantments(ItemStack itemStack){
        Map<Enchantment, Integer> itemStackEnchantmentData = EnchantmentHelper.getEnchantments(itemStack);
        LazyOptional<IBuiltInEnchantments> lazyCap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapabilityLazy(itemStack);
        if(lazyCap.isPresent()){
            Map<Enchantment, Integer> builtInEnchantments = lazyCap.resolve().get().getAllBuiltInEnchantmentDatas().stream()
                .collect(Collectors.groupingBy(enchantmentData -> enchantmentData.enchantment, Collectors.summingInt(value -> value.level)));
            builtInEnchantments.forEach((enchantment, integer) -> itemStackEnchantmentData.compute(enchantment, (enchantment1, integer1) -> integer1 == null ? integer : integer1 + integer));
        }
        enchantmentDataList = itemStackEnchantmentData.entrySet().stream()
            .map(entry -> new EnchantmentData(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    public int getEnchantmentLevel(Enchantment enchantment){
        return enchantmentDataList.stream()
            .filter(enchantmentData -> enchantmentData.enchantment == enchantment)
            .map(enchantmentData -> enchantmentData.level)
            .findFirst()
            .orElse(0);
    }

    public static final String ENCHANTMENT_DATA_KEY = "EnchantmentData";
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ListNBT enchantmentListnbt = new ListNBT();
        enchantmentDataList.forEach(enchantmentData -> {
            CompoundNBT enchantmentDataNBT = new CompoundNBT();
            enchantmentDataNBT.putString("id", String.valueOf(enchantmentData.enchantment.getRegistryName()));
            enchantmentDataNBT.putShort("lvl", (short)enchantmentData.level);
            enchantmentListnbt.add(enchantmentDataNBT);
        });
        nbt.put(ENCHANTMENT_DATA_KEY, enchantmentListnbt);
        return nbt;
    }

    public void deserializeNBT(CompoundNBT tag) {
        Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.deserializeEnchantments(tag.getList(ENCHANTMENT_DATA_KEY, 10));
        enchantmentDataList = enchantmentIntegerMap.entrySet().stream().map(entry -> new EnchantmentData(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

}

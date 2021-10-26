package com.infamous.dungeons_libraries.capabilities.builtinenchants;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import java.util.Map;


public class BuiltInEnchantmentsStorage implements Capability.IStorage<IBuiltInEnchantments> {

    public static final String ENCHANTS_KEY = "BuiltInEnchantments";
    public static final String SOURCE_KEY = "source";
    public static final String ENCHANTMENT_DATA_KEY = "data";

    @Override
    public INBT writeNBT(Capability<IBuiltInEnchantments> capability, IBuiltInEnchantments instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        ListNBT listnbt = new ListNBT();
        instance.getAllBuiltInEnchantmentDatasPerSource().forEach((resourceLocation, enchantmentDatas) -> {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putString(SOURCE_KEY, String.valueOf( resourceLocation));
            ListNBT enchantmentListnbt = new ListNBT();
            enchantmentDatas.forEach(enchantmentData -> {
                CompoundNBT enchantmentDataNBT = new CompoundNBT();
                enchantmentDataNBT.putString("id", String.valueOf(enchantmentData.enchantment.getRegistryName()));
                enchantmentDataNBT.putShort("lvl", (short)enchantmentData.level);
                enchantmentListnbt.add(enchantmentDataNBT);
            });
            compoundnbt.put(ENCHANTMENT_DATA_KEY, enchantmentListnbt);
            listnbt.add(compoundnbt);
        });
        tag.put(ENCHANTS_KEY, listnbt);
        return tag;
    }

    @Override
    public void readNBT(Capability<IBuiltInEnchantments> capability, IBuiltInEnchantments instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        ListNBT listNBT = tag.getList(ENCHANTS_KEY, 10);
        for(int i = 0; i < listNBT.size(); ++i) {
            CompoundNBT compoundnbt = listNBT.getCompound(i);
            ResourceLocation resourcelocation = ResourceLocation.tryParse(compoundnbt.getString(SOURCE_KEY));
            ListNBT enchantmentListnbt = new ListNBT();
            Map<Enchantment, Integer> enchantmentIntegerMap = EnchantmentHelper.deserializeEnchantments(compoundnbt.getList(ENCHANTMENT_DATA_KEY, 10));
            enchantmentIntegerMap.forEach((enchantment, integer) -> {
                instance.addBuiltInEnchantment(resourcelocation, new EnchantmentData(enchantment, integer));
            });
        }
    }
}
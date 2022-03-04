package com.infamous.dungeons_libraries.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

import static net.minecraftforge.registries.ForgeRegistries.ENCHANTMENTS;

public class Codecs {


    public static final Codec<EnchantmentData> ENCHANTMENT_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("enchantment").forGetter(data -> data.enchantment.getRegistryName()),
            Codec.INT.fieldOf("level").forGetter(data -> data.level)
    ).apply(instance, Codecs::getEnchantmentData));

    private static EnchantmentData getEnchantmentData(ResourceLocation resourceLocation, int level){
        Enchantment enchantment = ENCHANTMENTS.getValue(resourceLocation);
        return new EnchantmentData(enchantment, level);
    }

    public static final Codec<Rarity> ITEM_RARITY_CODEC = Codec.STRING.flatComapMap(Rarity::valueOf, d -> DataResult.success(d.name()));


}

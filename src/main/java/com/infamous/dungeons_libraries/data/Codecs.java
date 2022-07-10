package com.infamous.dungeons_libraries.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import static net.minecraftforge.registries.ForgeRegistries.ENCHANTMENTS;

public class Codecs {


    public static final Codec<EnchantmentInstance> ENCHANTMENT_DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("enchantment").forGetter(data -> data.enchantment.getRegistryName()),
            Codec.INT.fieldOf("level").forGetter(data -> data.level)
    ).apply(instance, Codecs::getEnchantmentInstance));

    private static EnchantmentInstance getEnchantmentInstance(ResourceLocation resourceLocation, int level){
        Enchantment enchantment = ENCHANTMENTS.getValue(resourceLocation);
        return new EnchantmentInstance(enchantment, level);
    }

    public static final Codec<Rarity> ITEM_RARITY_CODEC = Codec.STRING.flatComapMap(Rarity::valueOf, d -> DataResult.success(d.name()));


}

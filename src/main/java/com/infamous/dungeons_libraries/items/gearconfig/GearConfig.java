package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_libraries.data.Codecs.*;

public class GearConfig {

    public static final GearConfig DEFAULT = new GearConfig(new ArrayList<>(), new ArrayList<>(), false, Rarity.COMMON, 1);

    public static final Codec<GearConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(GearConfig::getAttributes),
            ENCHANTMENT_DATA_CODEC.listOf().optionalFieldOf("built_in_enchantments", new ArrayList<>()).forGetter(GearConfig::getBuiltInEnchantments),
            Codec.BOOL.optionalFieldOf("unique", false).forGetter(GearConfig::isUnique),
            ITEM_RARITY_CODEC.fieldOf("rarity").forGetter(GearConfig::getRarity),
            Codec.INT.optionalFieldOf("combo", 1).forGetter(GearConfig::getComboLength)
    ).apply(instance, GearConfig::new));

    private List<GearConfigAttributeModifier> attributes;
    private List<EnchantmentData> builtInEnchantments;
    private boolean unique;
    private Rarity rarity;
    private int comboLength;

    public GearConfig(List<GearConfigAttributeModifier> attributes, List<EnchantmentData> builtInEnchantments, boolean unique, Rarity rarity, int comboLength) {
        this.attributes = attributes;
        this.builtInEnchantments = builtInEnchantments;
        this.unique = unique;
        this.rarity = rarity;
        this.comboLength = comboLength;
    }

    public List<GearConfigAttributeModifier> getAttributes() {
        return attributes;
    }

    public List<EnchantmentData> getBuiltInEnchantments() {
        return builtInEnchantments;
    }

    public boolean isUnique() {
        return unique;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public int getComboLength() {
        return comboLength;
    }
}
//{
//    "parent": "SwordItem",
//    "attributes": [
//        {
//            "attribute": "minecraft:generic.attack_damage",
//            "value": 6
//        },
//        {
//            "attribute": "minecraft:generic.attack_speed",
//            "value": -2.4
//        }
//    ],
//    "built_in_enchantments": ["minecraft:sharpness"],
//    "unique": true,
//    "rarity": "rare",
//    "combo": 3
//}
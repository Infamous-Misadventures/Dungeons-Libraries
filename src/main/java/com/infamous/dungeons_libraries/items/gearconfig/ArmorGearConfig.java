package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterials;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_libraries.data.Codecs.ENCHANTMENT_DATA_CODEC;
import static com.infamous.dungeons_libraries.data.Codecs.ITEM_RARITY_CODEC;

public class ArmorGearConfig {

    public static final ArmorGearConfig DEFAULT = new ArmorGearConfig(new ArrayList<>(), new ArrayList<>(), new ResourceLocation("minecraft:iron"), false, Rarity.COMMON);

    public static final Codec<ArmorGearConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(ArmorGearConfig::getAttributes),
            ENCHANTMENT_DATA_CODEC.listOf().optionalFieldOf("built_in_enchantments", new ArrayList<>()).forGetter(ArmorGearConfig::getBuiltInEnchantments),
            ResourceLocation.CODEC.fieldOf("material").forGetter(armorGearConfig -> armorGearConfig.materialResource),
            Codec.BOOL.optionalFieldOf("unique", false).forGetter(ArmorGearConfig::isUnique),
            ITEM_RARITY_CODEC.fieldOf("rarity").forGetter(ArmorGearConfig::getRarity)
    ).apply(instance, ArmorGearConfig::new));

    private List<GearConfigAttributeModifier> attributes;
    private List<EnchantmentInstance> builtInEnchantments;
    private ResourceLocation materialResource;
    private boolean unique;
    private Rarity rarity;

    public ArmorGearConfig(List<GearConfigAttributeModifier> attributes, List<EnchantmentInstance> builtInEnchantments, ResourceLocation materialResource, boolean unique, Rarity rarity) {
        this.attributes = attributes;
        this.builtInEnchantments = builtInEnchantments;
        this.materialResource = materialResource;
        this.unique = unique;
        this.rarity = rarity;
    }

    public List<GearConfigAttributeModifier> getAttributes() {
        return attributes;
    }

    public List<EnchantmentInstance> getBuiltInEnchantments() {
        return builtInEnchantments;
    }

    public ArmorMaterial getArmorMaterial(){
        return DungeonsArmorMaterials.getArmorMaterial(materialResource);
    }

    public boolean isUnique() {
        return unique;
    }

    public Rarity getRarity() {
        return rarity;
    }

}
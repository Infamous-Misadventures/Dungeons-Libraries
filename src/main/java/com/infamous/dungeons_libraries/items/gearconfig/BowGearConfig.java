package com.infamous.dungeons_libraries.items.gearconfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_libraries.data.Codecs.ENCHANTMENT_DATA_CODEC;
import static com.infamous.dungeons_libraries.data.Codecs.ITEM_RARITY_CODEC;

public class BowGearConfig {

    public static final BowGearConfig DEFAULT = new BowGearConfig(new ArrayList<>(), new ArrayList<>(), 384, 20.0F, false, Rarity.COMMON);

    public static final Codec<BowGearConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(BowGearConfig::getAttributes),
            ENCHANTMENT_DATA_CODEC.listOf().optionalFieldOf("built_in_enchantments", new ArrayList<>()).forGetter(BowGearConfig::getBuiltInEnchantments),
            Codec.INT.optionalFieldOf("durability", 384).forGetter(BowGearConfig::getDurability),
            Codec.FLOAT.optionalFieldOf("default_charge_time", 20.0F).forGetter(BowGearConfig::getDefaultChargeTime),
            Codec.BOOL.optionalFieldOf("unique", false).forGetter(BowGearConfig::isUnique),
            ITEM_RARITY_CODEC.fieldOf("rarity").forGetter(BowGearConfig::getRarity)
    ).apply(instance, BowGearConfig::new));

    private final List<GearConfigAttributeModifier> attributes;
    private final List<EnchantmentInstance> builtInEnchantments;
    private final int durability;
    private final float defaultChargeTime;
    private final boolean unique;
    private final Rarity rarity;

    public BowGearConfig(List<GearConfigAttributeModifier> attributes, List<EnchantmentInstance> builtInEnchantments, int durability, float defaultChargeTime, boolean unique, Rarity rarity) {
        this.attributes = attributes;
        this.builtInEnchantments = builtInEnchantments;
        this.durability = durability;
        this.defaultChargeTime = defaultChargeTime;
        this.unique = unique;
        this.rarity = rarity;
    }

    public List<GearConfigAttributeModifier> getAttributes() {
        return attributes;
    }

    public List<EnchantmentInstance> getBuiltInEnchantments() {
        return builtInEnchantments;
    }

    public int getDurability() {
        return durability;
    }

    public float getDefaultChargeTime() {
        return defaultChargeTime;
    }

    public boolean isUnique() {
        return unique;
    }

    public Rarity getRarity() {
        return rarity;
    }

}
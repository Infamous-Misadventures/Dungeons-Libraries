package com.infamous.dungeons_libraries.items.gearconfig;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Rarity;

import java.util.ArrayList;
import java.util.List;

import static com.infamous.dungeons_libraries.data.Codecs.*;

public class MeleeGearConfig {

    public static final MeleeGearConfig DEFAULT = new MeleeGearConfig(new ArrayList<>(), new ArrayList<>(), false, false, Rarity.COMMON, 1);

    public static final Codec<MeleeGearConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(MeleeGearConfig::getAttributes),
            ENCHANTMENT_DATA_CODEC.listOf().optionalFieldOf("built_in_enchantments", new ArrayList<>()).forGetter(MeleeGearConfig::getBuiltInEnchantments),
            Codec.BOOL.optionalFieldOf("disables_shield", false).forGetter(MeleeGearConfig::isDisablesShield),
            Codec.BOOL.optionalFieldOf("unique", false).forGetter(MeleeGearConfig::isUnique),
            ITEM_RARITY_CODEC.fieldOf("rarity").forGetter(MeleeGearConfig::getRarity),
            Codec.INT.optionalFieldOf("combo", 1).forGetter(MeleeGearConfig::getComboLength)
    ).apply(instance, MeleeGearConfig::new));

    private List<GearConfigAttributeModifier> attributes;
    private List<EnchantmentData> builtInEnchantments;
    private boolean disablesShield;
    private boolean unique;
    private Rarity rarity;
    private int comboLength;

    public MeleeGearConfig(List<GearConfigAttributeModifier> attributes, List<EnchantmentData> builtInEnchantments, boolean disablesShield, boolean unique, Rarity rarity, int comboLength) {
        this.attributes = attributes;
        this.builtInEnchantments = builtInEnchantments;
        this.disablesShield = disablesShield;
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

    public boolean isDisablesShield() {
        return disablesShield;
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
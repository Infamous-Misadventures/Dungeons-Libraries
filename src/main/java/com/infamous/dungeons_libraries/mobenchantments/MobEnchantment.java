package com.infamous.dungeons_libraries.mobenchantments;

import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import static com.infamous.dungeons_libraries.mobenchantments.MobEnchantmentsRegistry.MOB_ENCHANTMENTS;

public class MobEnchantment extends ForgeRegistryEntry<MobEnchantment> {

    private Rarity rarity;
    private Type type;
    private String descriptionId;


    public MobEnchantment(Rarity rarity) {
        this.rarity = rarity;
        this.type = Type.ANY;
    }

    public MobEnchantment(Rarity rarity, Type type) {
        this.rarity = rarity;
        this.type = type;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Type getType() {
        return type;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("mobEnchantment", MOB_ENCHANTMENTS.getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.getDescriptionId());
    }

    public enum Rarity {
        COMMON(10),
        UNCOMMON(5),
        RARE(2),
        VERY_RARE(1);

        private final int weight;

        private Rarity(int rarityWeight) {
            this.weight = rarityWeight;
        }

        /**
         * Retrieves the weight of Rarity.
         */
        public int getWeight() {
            return this.weight;
        }
    }

    public enum Type {
        ANY,
        RANGED;
    }
}

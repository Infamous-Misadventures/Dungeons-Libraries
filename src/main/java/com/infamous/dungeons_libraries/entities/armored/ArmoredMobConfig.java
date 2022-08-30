package com.infamous.dungeons_libraries.entities.armored;

import com.infamous.dungeons_libraries.items.gearconfig.GearConfigAttributeModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;

public class ArmoredMobConfig extends WeightedRandom.Item {

    public static final ArmoredMobConfig DEFAULT = new ArmoredMobConfig(null, null, null, null, null, null, new ArrayList<>(), 0);

    public static final Codec<ArmoredMobConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("headItem", ItemStack.EMPTY).forGetter(armoredMobConfig -> armoredMobConfig.headItem),
            ItemStack.CODEC.optionalFieldOf("chestItem", ItemStack.EMPTY).forGetter(armoredMobConfig -> armoredMobConfig.chestItem),
            ItemStack.CODEC.optionalFieldOf("legsItem", ItemStack.EMPTY).forGetter(armoredMobConfig -> armoredMobConfig.legsItem),
            ItemStack.CODEC.optionalFieldOf("feetItem", ItemStack.EMPTY).forGetter(armoredMobConfig -> armoredMobConfig.feetItem),
            ItemStack.CODEC.optionalFieldOf("handItem", ItemStack.EMPTY).forGetter(armoredMobConfig -> armoredMobConfig.handItem),
            ItemStack.CODEC.optionalFieldOf("offhandItem", ItemStack.EMPTY).forGetter(armoredMobConfig -> armoredMobConfig.offhandItem),
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(armoredMobConfig -> armoredMobConfig.attributes),
            Codec.INT.fieldOf("weight").forGetter(armoredMobConfig -> armoredMobConfig.weight)
    ).apply(instance, ArmoredMobConfig::new));

    private final ItemStack headItem;
    private final ItemStack chestItem;
    private final ItemStack legsItem;
    private final ItemStack feetItem;
    private final ItemStack handItem;
    private final ItemStack offhandItem;
    private List<GearConfigAttributeModifier> attributes;

    public ArmoredMobConfig(ItemStack headItem, ItemStack chestItem, ItemStack legsItem, ItemStack feetItem, ItemStack handItem, ItemStack offhandItem, List<GearConfigAttributeModifier> attributes, int weight) {
        super(weight);
        this.attributes = attributes;
        this.headItem = headItem;
        this.chestItem = chestItem;
        this.legsItem = legsItem;
        this.feetItem = feetItem;
        this.handItem = handItem;
        this.offhandItem = offhandItem;
    }

    public ItemStack getHeadItem() {
        return headItem;
    }

    public ItemStack getChestItem() {
        return chestItem;
    }

    public ItemStack getLegsItem() {
        return legsItem;
    }

    public ItemStack getFeetItem() {
        return feetItem;
    }

    public ItemStack getHandItem() {
        return handItem;
    }

    public ItemStack getOffhandItem() {
        return offhandItem;
    }

    public List<GearConfigAttributeModifier> getAttributes() {
        return attributes;
    }

    public int getWeight() {
        return weight;
    }
}

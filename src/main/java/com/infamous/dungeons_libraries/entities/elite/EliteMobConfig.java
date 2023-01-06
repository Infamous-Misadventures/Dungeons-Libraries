package com.infamous.dungeons_libraries.entities.elite;

import com.infamous.dungeons_libraries.items.gearconfig.GearConfigAttributeModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EliteMobConfig implements WeightedEntry {

    public static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation("empty");
    public static final EliteMobConfig DEFAULT = new EliteMobConfig(Weight.of(0), null, null, null, null, null, null, new ArrayList<>(), EMPTY_TEXTURE);

    public static final Codec<EliteMobConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Weight.CODEC.fieldOf("weight").forGetter(eliteMobConfig -> eliteMobConfig.weight),
            ItemStack.CODEC.optionalFieldOf("headItem", ItemStack.EMPTY).forGetter(eliteMobConfig -> eliteMobConfig.headItem),
            ItemStack.CODEC.optionalFieldOf("chestItem", ItemStack.EMPTY).forGetter(eliteMobConfig -> eliteMobConfig.chestItem),
            ItemStack.CODEC.optionalFieldOf("legsItem", ItemStack.EMPTY).forGetter(eliteMobConfig -> eliteMobConfig.legsItem),
            ItemStack.CODEC.optionalFieldOf("feetItem", ItemStack.EMPTY).forGetter(eliteMobConfig -> eliteMobConfig.feetItem),
            ItemStack.CODEC.optionalFieldOf("handItem", ItemStack.EMPTY).forGetter(eliteMobConfig -> eliteMobConfig.handItem),
            ItemStack.CODEC.optionalFieldOf("offhandItem", ItemStack.EMPTY).forGetter(eliteMobConfig -> eliteMobConfig.offhandItem),
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(eliteMobConfig -> eliteMobConfig.attributes),
            ResourceLocation.CODEC.optionalFieldOf("texture", EMPTY_TEXTURE).forGetter(eliteMobConfig -> eliteMobConfig.texture)
    ).apply(instance, EliteMobConfig::new));

    private final ItemStack headItem;
    private final ItemStack chestItem;
    private final ItemStack legsItem;
    private final ItemStack feetItem;
    private final ItemStack handItem;
    private final ItemStack offhandItem;
    private final List<GearConfigAttributeModifier> attributes;
    private final ResourceLocation texture;
    private final Weight weight;

    public EliteMobConfig(Weight weight, ItemStack headItem, ItemStack chestItem, ItemStack legsItem, ItemStack feetItem, ItemStack handItem, ItemStack offhandItem, List<GearConfigAttributeModifier> attributes, ResourceLocation texture) {
        this.weight = weight;
        this.headItem = headItem;
        this.chestItem = chestItem;
        this.legsItem = legsItem;
        this.feetItem = feetItem;
        this.handItem = handItem;
        this.offhandItem = offhandItem;
        this.attributes = attributes;
        this.texture = texture;
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

    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public Weight getWeight() {
        return weight;
    }
}

package com.infamous.dungeons_libraries.items.materials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.List;

import static com.infamous.dungeons_libraries.items.materials.ArmorMaterialTypes.DUNGEONS;
import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class DungeonsArmorMaterial extends ArmorMaterial {

    public static final Codec<DungeonsArmorMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(DungeonsArmorMaterial::getName),
            Codec.INT.fieldOf("durability").forGetter(dungeonsArmorMaterial -> dungeonsArmorMaterial.durability),
            Codec.INT.listOf().fieldOf("damage_reduction_amounts").forGetter(dungeonsArmorMaterial -> dungeonsArmorMaterial.damageReductionAmounts),
            Codec.INT.fieldOf("enchantability").forGetter(DungeonsArmorMaterial::getEnchantmentValue),
            ResourceLocation.CODEC.fieldOf("repair_item").forGetter(dungeonsArmorMaterial -> dungeonsArmorMaterial.repairItemResourceLocation),
            SoundEvent.CODEC.fieldOf("equip_sound").forGetter(DungeonsArmorMaterial::getEquipSound),
            Codec.FLOAT.fieldOf("toughness").forGetter(DungeonsArmorMaterial::getToughness),
            Codec.FLOAT.fieldOf("knockback_resistance").forGetter(DungeonsArmorMaterial::getKnockbackResistance)
    ).apply(instance, DungeonsArmorMaterial::new));

    // Armor order: boots, leggings, chestplate, helmet
    private static final int[] BASE_DURABILITY_ARRAY = new int[]{13, 15, 16, 11};
    private final String name;
    private final SoundEvent equipSound;
    private final int durability;
    private final int enchantability;
    private final ResourceLocation repairItemResourceLocation;
    private final LazyValue<Ingredient> repairItem;
    private final List<Integer> damageReductionAmounts;
    private final float toughness;
    private final float knockbackResistance;

    private DungeonsArmorMaterial(String name, int durability, List<Integer> damageReductionAmounts, int enchantability, ResourceLocation repairItemResourceLocation, SoundEvent equipSound, float toughness, float knockbackResistance)
    {
        super(DUNGEONS);
        this.name = name;
        this.equipSound = equipSound;
        this.durability = durability;
        this.enchantability = enchantability;
        this.repairItemResourceLocation = repairItemResourceLocation;
        if(ITEMS.containsKey(repairItemResourceLocation)){
            Item item = ITEMS.getValue(repairItemResourceLocation);
            this.repairItem = new LazyValue<>(() -> Ingredient.of(item));
        }else{
            this.repairItem = new LazyValue<>(() -> Ingredient.of(Items.IRON_INGOT));
        }
        this.damageReductionAmounts = damageReductionAmounts;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slot)
    {
        return this.damageReductionAmounts.get(slot.getIndex());
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slot)
    {
        return BASE_DURABILITY_ARRAY[slot.getIndex()] * this.durability;
    }

    @Override
    public int getEnchantmentValue()
    {
        return this.enchantability;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Ingredient getRepairIngredient()
    {
        return this.repairItem.get();
    }

    @Override
    public SoundEvent getEquipSound()
    {
        return this.equipSound;
    }

    @Override
    public float getToughness()
    {
        return this.toughness;
    }

    //getKnockbackResistance
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}

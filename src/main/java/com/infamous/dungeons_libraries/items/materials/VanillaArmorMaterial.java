package com.infamous.dungeons_libraries.items.materials;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

import java.util.Arrays;

import static com.infamous.dungeons_libraries.items.materials.ArmorMaterialTypes.VANILLA;
import static net.minecraft.item.ArmorMaterial.IRON;

public class VanillaArmorMaterial extends ArmorMaterial {
    public static final VanillaArmorMaterial DEFAULT = new VanillaArmorMaterial("iron");

    public static final Codec<VanillaArmorMaterial> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(vanillaArmorMaterial -> vanillaArmorMaterial.materialName)
    ).apply(instance, VanillaArmorMaterial::new));

    private final String materialName;
    private final IArmorMaterial armorMaterial;

    private VanillaArmorMaterial(String materialName)
    {
        super(VANILLA);
        this.materialName = materialName;
        armorMaterial = Arrays.stream(net.minecraft.item.ArmorMaterial.values()).filter(armorMaterial1 -> materialName.equals(armorMaterial1.getName())).findFirst().orElse(IRON);
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType pSlot) {
        return armorMaterial.getDurabilityForSlot(pSlot);
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType pSlot) {
        return armorMaterial.getDefenseForSlot(pSlot);
    }

    @Override
    public int getEnchantmentValue() {
        return armorMaterial.getEnchantmentValue();
    }

    @Override
    public SoundEvent getEquipSound() {
        return armorMaterial.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return armorMaterial.getRepairIngredient();
    }

    @Override
    public String getName() {
        return armorMaterial.getName();
    }

    @Override
    public float getToughness() {
        return armorMaterial.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return armorMaterial.getKnockbackResistance();
    }
}

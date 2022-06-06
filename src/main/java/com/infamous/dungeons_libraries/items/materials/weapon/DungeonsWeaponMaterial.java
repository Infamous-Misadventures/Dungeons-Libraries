package com.infamous.dungeons_libraries.items.materials.weapon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class DungeonsWeaponMaterial implements IItemTier {

    public static final Codec<IItemTier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getName()),
            SoundEvent.CODEC.fieldOf("equip_sound").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getEquipSound()),
            Codec.INT.fieldOf("durability").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getUses()),
            Codec.INT.fieldOf("enchantability").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getEnchantmentValue()),
            ResourceLocation.CODEC.fieldOf("repair_item").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getRepairItemResourceLocation()),
            Codec.FLOAT.fieldOf("attack_speed").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getSpeed()),
            Codec.FLOAT.fieldOf("attack_damage").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getAttackDamageBonus()),
            Codec.INT.fieldOf("level").forGetter(iItemTier -> ((DungeonsWeaponMaterial) iItemTier).getLevel())
    ).apply(instance, DungeonsWeaponMaterial::new));
    
    private final String name;
    private final SoundEvent equipSound;
    private final int durability;
    private final int enchantability;
    private final ResourceLocation repairItemResourceLocation;
    private final Ingredient repairItem;
    private final float attackSpeed;
    private final float attackDamageBonus;
    private final int level;

    public DungeonsWeaponMaterial(String name, SoundEvent equipSound, int durability, int enchantability, ResourceLocation repairItemResourceLocation, float attackSpeed, float attackDamageBonus, int level) {
        this.name = name;
        this.equipSound = equipSound;
        this.durability = durability;
        this.enchantability = enchantability;
        this.repairItemResourceLocation = repairItemResourceLocation;
        if(ITEMS.containsKey(repairItemResourceLocation)){
            Item item = ITEMS.getValue(repairItemResourceLocation);
            this.repairItem = Ingredient.of(item);
        }else{
            this.repairItem = Ingredient.of(Items.IRON_INGOT);
        }
        this.attackSpeed = attackSpeed;
        this.attackDamageBonus = attackDamageBonus;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public int getUses() {
        return durability;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    public ResourceLocation getRepairItemResourceLocation() {
        return repairItemResourceLocation;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairItem;
    }

    @Override
    public float getSpeed() {
        return attackSpeed;
    }

    @Override
    public float getAttackDamageBonus() {
        return attackDamageBonus;
    }

    @Override
    public int getLevel() {
        return level;
    }


}

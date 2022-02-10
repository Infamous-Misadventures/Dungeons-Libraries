package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.items.interfaces.IComboWeapon;
import com.infamous.dungeons_libraries.items.interfaces.IMeleeWeapon;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_DAMAGE;
import static net.minecraft.entity.ai.attributes.Attributes.ATTACK_SPEED;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class SwordGearConfig extends SwordItem implements IMeleeWeapon, IComboWeapon {

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private GearConfig gearConfig;

    public SwordGearConfig(IItemTier tier, Item.Properties properties) {
        super(tier,
                1, -2.4F,
                properties);
        loadConfig();
    }

    public void loadConfig(){
        gearConfig = GearConfigRegistry.getConfig(this.getRegistryName());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        gearConfig.getAttributes().forEach(attributeModifier -> {
            Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
            UUID uuid = randomUUID();
            if(ATTACK_DAMAGE.equals(attribute)){
                uuid = BASE_ATTACK_DAMAGE_UUID;
            }else if(ATTACK_SPEED.equals(attribute)){
                uuid = BASE_ATTACK_SPEED_UUID;
            }
            builder.put(attribute, new AttributeModifier(uuid, "Weapon modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
        });
        this.defaultModifiers = builder.build();
    }

    @Override
    public int getComboLength(ItemStack stack, LivingEntity attacker) {
        return 0;
    }

    @Override
    public boolean isUnique() {
        return gearConfig.isUnique();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlotType.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }
}

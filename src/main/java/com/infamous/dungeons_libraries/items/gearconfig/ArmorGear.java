package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.items.gearconfig.client.ArmorGearModels;
import com.infamous.dungeons_libraries.items.interfaces.IArmor;
import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import com.infamous.dungeons_libraries.items.interfaces.IUniqueGear;
import com.infamous.dungeons_libraries.mixin.ArmorItemAccessor;
import com.infamous.dungeons_libraries.mixin.ItemAccessor;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.minecraft.item.ArmorMaterial.CHAIN;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class ArmorGear extends ArmorItem implements IReloadableGear, IArmor, IUniqueGear {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private ArmorGearConfig armorGearConfig;
    private ResourceLocation texture;

    public ArmorGear(EquipmentSlotType slotType, Properties properties, ResourceLocation texture) {
        super(CHAIN, slotType, properties);
        this.texture = texture;
        reload();
    }

    @Override
    public void reload(){
        armorGearConfig = ArmorGearConfigRegistry.getConfig(this.getRegistryName());
        IArmorMaterial material = armorGearConfig.getArmorMaterial();
        ((ArmorItemAccessor)this).setMaterial(material);
        ((ArmorItemAccessor)this).setDefense(material.getDefenseForSlot(this.slot));
        ((ArmorItemAccessor)this).setToughness(material.getToughness());
        ((ArmorItemAccessor)this).setKnockbackResistance(material.getKnockbackResistance());
        ((ItemAccessor)this).setMaxDamage(material.getDurabilityForSlot(this.slot));
        armorGearConfig = ArmorGearConfigRegistry.getConfig(this.getRegistryName());
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        UUID primaryUuid = ARMOR_MODIFIER_UUID_PER_SLOT[this.slot.getIndex()];
        builder.put(Attributes.ARMOR, new AttributeModifier(primaryUuid, "Armor modifier", (double)material.getDefenseForSlot(this.slot), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(primaryUuid, "Armor toughness", (double)material.getToughness(), AttributeModifier.Operation.ADDITION));
        if (this.knockbackResistance > 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(primaryUuid, "Armor knockback resistance", (double)this.knockbackResistance, AttributeModifier.Operation.ADDITION));
        }
        armorGearConfig.getAttributes().forEach(attributeModifier -> {
            Attribute attribute = ATTRIBUTES.getValue(attributeModifier.getAttributeResourceLocation());
            if(attribute != null) {
                UUID uuid = randomUUID();
                builder.put(attribute, new AttributeModifier(uuid, "Armor modifier", attributeModifier.getAmount(), attributeModifier.getOperation()));
            }
        });
        this.defaultModifiers = builder.build();
    }

    public ArmorGearConfig getGearConfig() {
        return armorGearConfig;
    }

    @Override
    public boolean isUnique() {
        return armorGearConfig.isUnique();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType pEquipmentSlot) {
        return pEquipmentSlot == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flag)
    {
        super.appendHoverText(stack, world, list, flag);
        DescriptionHelper.addLoreDescription(list, stack);
    }

    @Override
    public Rarity getRarity(ItemStack pStack) {
        return getGearConfig().getRarity();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return (A) ArmorGearModels.getModel(this.getRegistryName()).apply(1.0F, armorSlot, entityLiving);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return texture.toString();
    }

}
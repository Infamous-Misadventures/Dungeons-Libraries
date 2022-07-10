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
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static net.minecraft.world.item.ArmorMaterials.CHAIN;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class ArmorGear extends ArmorItem implements IReloadableGear, IArmor, IUniqueGear {
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private ArmorGearConfig armorGearConfig;
    private ResourceLocation texture;

    public ArmorGear(EquipmentSlot slotType, Properties properties, ResourceLocation texture) {
        super(CHAIN, slotType, properties);
        this.texture = texture;
        reload();
    }

    @Override
    public void reload(){
        armorGearConfig = ArmorGearConfigRegistry.getConfig(this.getRegistryName());
        ArmorMaterial material = armorGearConfig.getArmorMaterial();
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
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == this.slot ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }



    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> list, TooltipFlag flag)
    {
        super.appendHoverText(stack, level, list, flag);
        DescriptionHelper.addLoreDescription(list, stack);
    }


    @Override
    public Rarity getRarity(ItemStack pStack) {
        return getGearConfig().getRarity();
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer)
    {
        consumer.accept(new IItemRenderProperties()
        {
            @Nullable
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return ArmorGearModels.getModel(itemStack.getItem().getRegistryName()).apply(1.0F, armorSlot, entityLiving);
            }
        });
    }



    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return texture.toString();
    }

}
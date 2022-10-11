package com.infamous.dungeons_libraries.items.gearconfig;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.items.interfaces.IArmor;
import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import com.infamous.dungeons_libraries.items.interfaces.IUniqueGear;
import com.infamous.dungeons_libraries.mixin.ArmorItemAccessor;
import com.infamous.dungeons_libraries.mixin.ItemAccessor;
import com.infamous.dungeons_libraries.utils.DescriptionHelper;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static java.util.UUID.randomUUID;
import static net.minecraft.world.item.ArmorMaterials.CHAIN;
import static net.minecraftforge.registries.ForgeRegistries.ATTRIBUTES;

public class ArmorGear extends GeoArmorItem implements IReloadableGear, IArmor, IUniqueGear, IAnimatable {
    private static final ResourceLocation DEFAULT_ARMOR_ANIMATIONS = new ResourceLocation(DungeonsLibraries.MODID, "animations/armor/armor_default.animation.json");
    private static final UUID[] ARMOR_MODIFIER_UUID_PER_SLOT = new UUID[]{UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")};

    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private ArmorGearConfig armorGearConfig;
    private ResourceLocation armorSet;
    private ResourceLocation modelLocation;
    private ResourceLocation textureLocation;
    private ResourceLocation animationFileLocation;

    public ArmorGear(EquipmentSlot slotType, Properties properties, ResourceLocation armorSet, ResourceLocation modelLocation, ResourceLocation textureLocation, ResourceLocation animationFileLocation) {
        super(CHAIN, slotType, properties);
        this.armorSet = armorSet;
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
        this.animationFileLocation = animationFileLocation;
        reload();
    }

    @Override
    public void reload(){
        armorGearConfig = ArmorGearConfigRegistry.getConfig(this.armorSet);
        if(armorGearConfig == ArmorGearConfig.DEFAULT){
            armorGearConfig = ArmorGearConfigRegistry.getConfig(this.getRegistryName());
        }
        ArmorMaterial material = armorGearConfig.getArmorMaterial();
        ((ArmorItemAccessor)this).setMaterial(material);
        ((ArmorItemAccessor)this).setDefense(material.getDefenseForSlot(this.slot));
        ((ArmorItemAccessor)this).setToughness(material.getToughness());
        ((ArmorItemAccessor)this).setKnockbackResistance(material.getKnockbackResistance());
        ((ItemAccessor)this).setMaxDamage(material.getDurabilityForSlot(this.slot));
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
        if(armorSet != null) {
            DescriptionHelper.addLoreDescription(list, armorSet);
        }else {
            DescriptionHelper.addLoreDescription(list, this.getRegistryName());
        }
    }


    @Override
    public Rarity getRarity(ItemStack pStack) {
        return getGearConfig().getRarity();
    }

    public ResourceLocation getArmorSet() {
        return armorSet;
    }

    protected AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller",
            20, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    public ResourceLocation getModelLocation() {
        return modelLocation;
    }

    public ResourceLocation getTextureLocation() {
        return textureLocation;
    }

    public ResourceLocation getAnimationFileLocation() {
        return animationFileLocation;
    }
    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IItemRenderProperties() {
            @Override
            public HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
                                                  EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return (HumanoidModel<?>) GeoArmorRenderer.getRenderer(ArmorGear.class, entityLiving).applyEntityStats(_default)
                    .setCurrentItem(entityLiving, itemStack, armorSlot).applySlot(armorSlot);
            }
        });
    }
}
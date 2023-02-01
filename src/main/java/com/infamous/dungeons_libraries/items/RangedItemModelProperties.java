package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

import static com.infamous.dungeons_libraries.utils.RangedAttackHelper.getCrossbowChargeTime;

public class RangedItemModelProperties {

    private static final ResourceLocation PULL_PROPERTY = new ResourceLocation("pull");
    private static final ResourceLocation PULLING_PROPERTY = new ResourceLocation("pulling");
    private static final ResourceLocation CHARGED_PROPERTY = new ResourceLocation("charged");

    public static void init() {
        Map<Item, Map<ResourceLocation, ItemPropertyFunction>> itemModelsProperties = ItemProperties.PROPERTIES;

        Map<ResourceLocation, ItemPropertyFunction> bowModelProperties = itemModelsProperties.get(Items.BOW);
        bowModelProperties.put(PULL_PROPERTY,
                RangedItemModelProperties::getBowPullProperty);
        bowModelProperties.put(PULLING_PROPERTY,
                RangedItemModelProperties::getBowPullingProperty);

        Map<ResourceLocation, ItemPropertyFunction> crossbowModelProperties = itemModelsProperties.get(Items.CROSSBOW);
        crossbowModelProperties.put(PULL_PROPERTY,
                RangedItemModelProperties::getCrossbowPullProperty);
        crossbowModelProperties.put(PULLING_PROPERTY,
                RangedItemModelProperties::getCrossbowPullingProperty);
        crossbowModelProperties.put(CHARGED_PROPERTY,
                RangedItemModelProperties::getCrossbowChargedProperty);
    }

    public static void addRangedModelProperties(RegistryObject<Item> itemRegistryObject) {
        if (itemRegistryObject.get() instanceof BowItem) {
            addBowModelProperties(itemRegistryObject);
        } else if (itemRegistryObject.get() instanceof CrossbowItem) {
            addCrossbowModelProperties(itemRegistryObject);
        }
    }

    public static void addBowModelProperties(RegistryObject<Item> itemRegistryObject) {
        ItemProperties.register(itemRegistryObject.get(), PULL_PROPERTY,
                RangedItemModelProperties::getBowPullProperty);
        ItemProperties.register(itemRegistryObject.get(), PULLING_PROPERTY,
                RangedItemModelProperties::getBowPullingProperty);
    }

    public static void addCrossbowModelProperties(RegistryObject<Item> itemRegistryObject) {
        ItemProperties.register(itemRegistryObject.get(), PULL_PROPERTY,
                RangedItemModelProperties::getCrossbowPullProperty);
        ItemProperties.register(itemRegistryObject.get(), PULLING_PROPERTY,
                RangedItemModelProperties::getCrossbowPullingProperty);
        ItemProperties.register(itemRegistryObject.get(), CHARGED_PROPERTY,
                RangedItemModelProperties::getCrossbowChargedProperty);
    }

    private static float getCrossbowPullProperty(ItemStack stack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
        if (livingEntity == null || CrossbowItem.isCharged(stack)) {
            return 0.0F;
        } else return (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                / getCrossbowChargeTime(livingEntity, stack);
    }

    private static float getCrossbowPullingProperty(ItemStack stack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
        return livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getUseItem() == stack && !CrossbowItem.isCharged(stack)
                ? 1.0F
                : 0.0F;
    }

    private static float getCrossbowChargedProperty(ItemStack stack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
        return livingEntity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
    }

    private static float getBowPullProperty(ItemStack stack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
        if (livingEntity == null || livingEntity.getUseItem() != stack) {
            return 0.0F;
        } else {
            return (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                    / RangedAttackHelper.getBowChargeTime(livingEntity, livingEntity.getUseItem());
        }
    }

    private static float getBowPullingProperty(ItemStack stack, ClientLevel clientWorld, LivingEntity livingEntity, int i) {
        return livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getUseItem() == stack
                ? 1.0F
                : 0.0F;
    }
}

package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGear;
import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

import java.util.Map;

import static com.infamous.dungeons_libraries.utils.RangedAttackHelper.getModdedCrossbowChargeTime;
import static com.infamous.dungeons_libraries.utils.RangedAttackHelper.getVanillaCrossbowChargeTime;

public class RangedItemModelProperties {

    private static final ResourceLocation PULL_PROPERTY = new ResourceLocation("pull");
    private static final ResourceLocation PULLING_PROPERTY = new ResourceLocation("pulling");
    private static final ResourceLocation CHARGED_PROPERTY = new ResourceLocation("charged");

    public static void init(){
        Map<Item, Map<ResourceLocation, IItemPropertyGetter>> itemModelsProperties = ItemModelsProperties.PROPERTIES;

        Map<ResourceLocation, IItemPropertyGetter> bowModelProperties = itemModelsProperties.get(Items.BOW);
        bowModelProperties.put(PULL_PROPERTY,
                RangedItemModelProperties::getBowPullProperty);
        bowModelProperties.put(PULLING_PROPERTY,
                RangedItemModelProperties::getBowPullingProperty);

        Map<ResourceLocation, IItemPropertyGetter> crossbowModelProperties = itemModelsProperties.get(Items.CROSSBOW);
        crossbowModelProperties.put(PULL_PROPERTY,
                RangedItemModelProperties::getCrossbowPullProperty);
        crossbowModelProperties.put(PULLING_PROPERTY,
                RangedItemModelProperties::getCrossbowPullingProperty);
        crossbowModelProperties.put(CHARGED_PROPERTY,
                RangedItemModelProperties::getCrossbowChargedProperty);
    }

    public static void addRangedModelProperties(RegistryObject<Item> itemRegistryObject){
        if(itemRegistryObject.get() instanceof BowItem) {
            addBowModelProperties(itemRegistryObject);
        } else if(itemRegistryObject.get() instanceof CrossbowItem) {
            addCrossbowModelProperties(itemRegistryObject);
        }
    }

    public static void addBowModelProperties(RegistryObject<Item> itemRegistryObject){
        ItemModelsProperties.register(itemRegistryObject.get(), PULL_PROPERTY,
                RangedItemModelProperties::getBowPullProperty);
        ItemModelsProperties.register(itemRegistryObject.get(), PULLING_PROPERTY,
                RangedItemModelProperties::getBowPullingProperty);
    }

    public static void addCrossbowModelProperties(RegistryObject<Item> itemRegistryObject){
        ItemModelsProperties.register(itemRegistryObject.get(), PULL_PROPERTY,
                RangedItemModelProperties::getCrossbowPullProperty);
        ItemModelsProperties.register(itemRegistryObject.get(), PULLING_PROPERTY,
                RangedItemModelProperties::getCrossbowPullingProperty);
        ItemModelsProperties.register(itemRegistryObject.get(), CHARGED_PROPERTY,
                RangedItemModelProperties::getCrossbowChargedProperty);
    }

    private static float getCrossbowPullProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        if (livingEntity == null || CrossbowItem.isCharged(stack)) {
            return 0.0F;
        } else if(stack.getItem() instanceof CrossbowGear) {
            return (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                    / getModdedCrossbowChargeTime(livingEntity, stack);
        } else {
            return CrossbowItem.isCharged(stack) ? 0.0F
                    : (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                    / getVanillaCrossbowChargeTime(livingEntity, stack);
        }
    }

    private static float getCrossbowPullingProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        return livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getUseItem() == stack && !CrossbowItem.isCharged(stack)
                ? 1.0F
                : 0.0F;
    }

    private static float getCrossbowChargedProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        return livingEntity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
    }

    private static float getBowPullProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        if (livingEntity == null || livingEntity.getUseItem() != stack) {
            return 0.0F;
        }else{
            return (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                    / RangedAttackHelper.getBowChargeTime(livingEntity, livingEntity.getUseItem());
        }
    }

    private static float getBowPullingProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        return livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getUseItem() == stack
                ? 1.0F
                : 0.0F;
    }
}

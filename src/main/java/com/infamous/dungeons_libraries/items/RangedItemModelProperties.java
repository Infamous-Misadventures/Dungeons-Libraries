package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.items.gearconfig.BowGear;
import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGear;
import com.infamous.dungeons_libraries.utils.RangedAttackHelper;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

import static com.infamous.dungeons_libraries.utils.RangedAttackHelper.getModdedCrossbowChargeTime;
import static com.infamous.dungeons_libraries.utils.RangedAttackHelper.getVanillaCrossbowChargeTime;

public class RangedItemModelProperties {
    public RangedItemModelProperties(){
        Map<Item, Map<ResourceLocation, IItemPropertyGetter>> itemModelsProperties = ItemModelsProperties.PROPERTIES;

        Map<ResourceLocation, IItemPropertyGetter> bowModelProperties = itemModelsProperties.get(Items.BOW);
        bowModelProperties.put(new ResourceLocation("pull"),
                this::getBowPullProperty);
        bowModelProperties.put(new ResourceLocation("pulling"),
                this::getBowPullingProperty);

        Map<ResourceLocation, IItemPropertyGetter> crossbowModelProperties = itemModelsProperties.get(Items.CROSSBOW);
        crossbowModelProperties.put(new ResourceLocation("pull"),
                this::getCrossbowPullProperty);
        crossbowModelProperties.put(new ResourceLocation("pulling"),
                this::getCrossbowPullingProperty);
        crossbowModelProperties.put(new ResourceLocation("charged"),
                this::getCrossbowChargedProperty);
    }

    private float getCrossbowPullProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
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

    private float getCrossbowPullingProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        return livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getUseItem() == stack && !CrossbowItem.isCharged(stack)
                ? 1.0F
                : 0.0F;
    }

    private float getCrossbowChargedProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        return livingEntity != null && CrossbowItem.isCharged(stack) ? 1.0F : 0.0F;
    }

    private float getBowPullProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        if (livingEntity == null || livingEntity.getUseItem() != stack) {
            return 0.0F;
        }else{
            return (stack.getUseDuration() - livingEntity.getUseItemRemainingTicks())
                    / RangedAttackHelper.getBowChargeTime(livingEntity, livingEntity.getUseItem());
        }
    }

    private float getBowPullingProperty(ItemStack stack, ClientWorld clientWorld, LivingEntity livingEntity) {
        return livingEntity != null && livingEntity.isUsingItem()
                && livingEntity.getUseItem() == stack
                ? 1.0F
                : 0.0F;
    }
}

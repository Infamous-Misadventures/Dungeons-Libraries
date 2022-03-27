package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry.GEAR_CONFIG_BUILTIN_RESOURCELOCATION;

@Mod.EventBusSubscriber(modid = MODID, value= Dist.CLIENT)
public class DescriptionHelper {

    // Rewrite to a mixin inside ItemStack::getTooltipLines. Figure out a way to have all styles available.
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event){
        LazyOptional<IBuiltInEnchantments> lazyCap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapabilityLazy(event.getItemStack());
        lazyCap.ifPresent(cap -> {
            List<EnchantmentData> builtInEnchantments = cap.getBuiltInEnchantments(GEAR_CONFIG_BUILTIN_RESOURCELOCATION);
            builtInEnchantments.forEach(enchantmentData -> {
                event.getToolTip().add(enchantmentData.enchantment.getFullname(enchantmentData.level).copy().withStyle(Style.EMPTY.withColor(Color.parseColor("#FF8100"))));
            });
        });
    }

    public static void addFullDescription(List<ITextComponent> list, ItemStack itemStack){
        addLoreDescription(list, itemStack);
    }

    public static void addLoreDescription(List<ITextComponent> list, ItemStack itemStack){
        list.add(new TranslationTextComponent(
                "lore.dungeons_gear." + itemStack.getItem().getRegistryName().getPath())
                .withStyle(TextFormatting.WHITE, TextFormatting.ITALIC));
    }

//    public static void addChargeableDescription(List<ITextComponent> list, ItemStack itemStack) {
//        if(itemStack.getItem() instanceof IChargeableItem){
//            IChargeableItem chargeableItem = (IChargeableItem) itemStack.getItem();
//            int chargeTimeInSeconds = chargeableItem.getChargeTimeInSeconds();
//            if(chargeTimeInSeconds > 0) {
//                list.add(new TranslationTextComponent(
//                        "artifact.dungeons_gear.charge_time", chargeTimeInSeconds)
//                        .withStyle(TextFormatting.BLUE));
//            }
//        }
//    }

//    public static voi>d addArtifactInfo(List<ITextComponent> list, ItemStack itemStack) {
//        if (itemStack.getItem() instanceof ArtifactItem) {
//
//            list.add(new TranslationTextComponent(
//                    "ability.dungeons_gear." + itemStack.getItem().getRegistryName().getPath())
//                    .withStyle(TextFormatting.GREEN));
//
//            ArtifactItem artifactItem = (ArtifactItem) itemStack.getItem();
//            int durationInSeconds = artifactItem.getDurationInSeconds();
//            int cooldownInSeconds = artifactItem.getCooldownInSeconds();
//
//            if(durationInSeconds > 0) {
//                list.add(new TranslationTextComponent(
//                        "artifact.dungeons_gear.duration", durationInSeconds)
//                        .withStyle(TextFormatting.BLUE));
//            }
//            if(cooldownInSeconds > 0) {
//                list.add(new TranslationTextComponent(
//                        "artifact.dungeons_gear.cooldown", cooldownInSeconds)
//                        .withStyle(TextFormatting.BLUE));
//            }
//        }
//    }
}

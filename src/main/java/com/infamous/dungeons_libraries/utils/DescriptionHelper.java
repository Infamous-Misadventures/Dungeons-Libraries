package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry.GEAR_CONFIG_BUILTIN_RESOURCELOCATION;

@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class DescriptionHelper {

    // Rewrite to a mixin inside ItemStack::getTooltipLines. Figure out a way to have all styles available.
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        BuiltInEnchantments cap = BuiltInEnchantmentsHelper.getBuiltInEnchantmentsCapability(event.getItemStack());
        List<EnchantmentInstance> builtInEnchantments = cap.getBuiltInEnchantments(GEAR_CONFIG_BUILTIN_RESOURCELOCATION);
        builtInEnchantments.forEach(enchantmentInstance -> {
            event.getToolTip().add(enchantmentInstance.enchantment.getFullname(enchantmentInstance.level).copy().withStyle(Style.EMPTY.withColor(TextColor.parseColor("#FF8100"))));
        });
    }

    public static void addArtifactDescription(List<Component> list, ItemStack itemStack){
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        if(registryName == null) return;
        addLoreDescription(list, registryName);
        addArtifactInfo(list, itemStack);
    }

    public static void addArtifactInfo(List<Component> list, ItemStack itemStack) {
        if (itemStack.getItem() instanceof ArtifactItem) {

            list.add(new TranslatableComponent(
                    "artifact.dungeons_libraries.base")
                    .withStyle(ChatFormatting.DARK_AQUA));

            ResourceLocation registryName = itemStack.getItem().getRegistryName();
            list.add(new TranslatableComponent(
                    "ability." + registryName.getNamespace() + "." + registryName.getPath())
                    .withStyle(ChatFormatting.GREEN));

            ArtifactItem artifactItem = (ArtifactItem) itemStack.getItem();
            int durationInSeconds = artifactItem.getDurationInSeconds();
            int cooldownInSeconds = artifactItem.getCooldownInSeconds();

            if(durationInSeconds > 0) {
                list.add(new TranslatableComponent(
                        "artifact.dungeons_libraries.duration", durationInSeconds)
                        .withStyle(ChatFormatting.BLUE));
            }
            if(cooldownInSeconds > 0) {
                list.add(new TranslatableComponent(
                        "artifact.dungeons_libraries.cooldown", cooldownInSeconds)
                        .withStyle(ChatFormatting.BLUE));
            }
        }
    }

    public static void addFullDescription(List<Component> list, ItemStack itemStack) {
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
        addLoreDescription(list, registryName);
    }

    public static void addLoreDescription(List<Component> list, ResourceLocation registryName) {
        list.add(new TranslatableComponent(
                "lore." + registryName.getNamespace() + "." + registryName.getPath())
                .withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }

    public static void addChargeableDescription(List<Component> list, ItemStack itemStack) {
//        if(itemStack.getItem() instanceof IChargeableItem){
//            IChargeableItem chargeableItem = (IChargeableItem) itemStack.getItem();
//            int chargeTimeInSeconds = chargeableItem.getChargeTimeInSeconds();
//            if(chargeTimeInSeconds > 0) {
//                list.add(new TranslatableComponent(
//                        "artifact.dungeons_gear.charge_time", chargeTimeInSeconds)
//                        .withStyle(ChatFormatting.BLUE));
//            }
//        }
    }
}

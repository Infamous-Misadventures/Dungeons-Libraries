package com.infamous.dungeons_libraries.client;

import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class TooltipHelper {

    public static void addFullDescription(List<Component> list, ItemStack itemStack){
        addLoreDescription(list, itemStack);
        addArtifactInfo(list, itemStack);
        addChargeableDescription(list, itemStack);
    }

    public static void addLoreDescription(List<Component> list, ItemStack itemStack){
        ResourceLocation registryName = itemStack.getItem().getRegistryName();
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

}

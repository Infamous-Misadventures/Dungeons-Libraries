package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsHelper;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
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

    public static void addFullDescription(List<Component> list, ItemStack itemStack) {
        addLoreDescription(list, itemStack);
    }

    public static void addLoreDescription(List<Component> list, ItemStack itemStack) {
        list.add(new TranslatableComponent(
                "lore.dungeons_gear." + itemStack.getItem().getRegistryName().getPath())
                .withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }

    public static void addLoreDescription(List<Component> list, ResourceLocation registryName) {
        list.add(new TranslatableComponent(
                "lore." + registryName.getNamespace() + "." + registryName.getPath())
                .withStyle(ChatFormatting.WHITE, ChatFormatting.ITALIC));
    }
}

package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider;
import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableProvider;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class ItemEvents {
    @SubscribeEvent
    public static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (canBeEnchanted(event.getObject())) {
            event.addCapability(new ResourceLocation(MODID, "built_in_enchantments"), new BuiltInEnchantmentsProvider());
        }
    }

    private static boolean canBeEnchanted(ItemStack itemStack){
        return Arrays.stream(EnchantmentType.values()).anyMatch(enchantmentType -> enchantmentType.canEnchant(itemStack.getItem()));
    }
}
package com.infamous.dungeons_libraries.combat;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class TwoHandedHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!DungeonsLibrariesConfig.ENABLE_TWO_HANDED_WEAPON.get()) return;
        MeleeGearConfig configTo = MeleeGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(event.getTo().getItem()));
        if (configTo.isTwoHanded()) {
            if (event.getSlot().equals(EquipmentSlot.MAINHAND)) {
                ItemStack offhandItem = event.getEntity().getOffhandItem();
                if (!offhandItem.isEmpty()) {
                    event.getEntity().setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    event.getEntity().spawnAtLocation(offhandItem);
                }
            } else if (event.getSlot().equals(EquipmentSlot.OFFHAND)) {
                ItemStack mainhandItem = event.getEntity().getMainHandItem();
                event.getEntity().setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, event.getTo());
                if (!mainhandItem.isEmpty()) {
                    event.getEntity().spawnAtLocation(mainhandItem);
                }
            }
        } else if (!event.getTo().isEmpty()) {
            ItemStack mainhandItem = event.getEntity().getMainHandItem();
            MeleeGearConfig configMainHand = MeleeGearConfigRegistry.getConfig(ForgeRegistries.ITEMS.getKey(mainhandItem.getItem()));
            if (configMainHand.isTwoHanded() && event.getSlot().equals(EquipmentSlot.OFFHAND)) {
                event.getEntity().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                event.getEntity().spawnAtLocation(mainhandItem);
            }
        }

    }

}

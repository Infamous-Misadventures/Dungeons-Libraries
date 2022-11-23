package com.infamous.dungeons_libraries.combat;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class TwoHandedHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event){
        MeleeGearConfig configTo = MeleeGearConfigRegistry.getConfig(event.getTo().getItem().getRegistryName());
        if(configTo.isTwoHanded()){
            if(event.getSlot().equals(EquipmentSlotType.MAINHAND)){
                ItemStack offhandItem = event.getEntityLiving().getOffhandItem();
                if(!offhandItem.isEmpty()){
                    event.getEntityLiving().setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                    event.getEntityLiving().spawnAtLocation(offhandItem);
                }
            }
            else if(event.getSlot().equals(EquipmentSlotType.OFFHAND)){
                ItemStack mainhandItem = event.getEntityLiving().getMainHandItem();
                event.getEntityLiving().setItemInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                event.getEntityLiving().setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                event.getEntityLiving().setItemInHand(Hand.MAIN_HAND, event.getTo());
                if(!mainhandItem.isEmpty()){
                    event.getEntityLiving().spawnAtLocation(mainhandItem);
                }
            }
        } else if(!event.getTo().isEmpty()){
            ItemStack mainhandItem = event.getEntityLiving().getMainHandItem();
            MeleeGearConfig configMainHand = MeleeGearConfigRegistry.getConfig(mainhandItem.getItem().getRegistryName());
            if (configMainHand.isTwoHanded() && event.getSlot().equals(EquipmentSlotType.OFFHAND)) {
                event.getEntityLiving().setItemInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                event.getEntityLiving().spawnAtLocation(mainhandItem);
            }
        }

    }

}

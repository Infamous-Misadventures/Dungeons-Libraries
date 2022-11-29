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

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class TwoHandedHandler {

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event){
        if(!DungeonsLibrariesConfig.ENABLE_TWO_HANDED_WEAPON.get()) return;
        MeleeGearConfig configTo = MeleeGearConfigRegistry.getConfig(event.getTo().getItem().getRegistryName());
        if(configTo.isTwoHanded()){
            if(event.getSlot().equals(EquipmentSlot.MAINHAND)){
                ItemStack offhandItem = event.getEntityLiving().getOffhandItem();
                if(!offhandItem.isEmpty()){
                    event.getEntityLiving().setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    event.getEntityLiving().spawnAtLocation(offhandItem);
                }
            }
            else if(event.getSlot().equals(EquipmentSlot.OFFHAND)){
                ItemStack mainhandItem = event.getEntityLiving().getMainHandItem();
                event.getEntityLiving().setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                event.getEntityLiving().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                event.getEntityLiving().setItemInHand(InteractionHand.MAIN_HAND, event.getTo());
                if(!mainhandItem.isEmpty()){
                    event.getEntityLiving().spawnAtLocation(mainhandItem);
                }
            }
        } else if(!event.getTo().isEmpty()){
            ItemStack mainhandItem = event.getEntityLiving().getMainHandItem();
            MeleeGearConfig configMainHand = MeleeGearConfigRegistry.getConfig(mainhandItem.getItem().getRegistryName());
            if (configMainHand.isTwoHanded() && event.getSlot().equals(EquipmentSlot.OFFHAND)) {
                event.getEntityLiving().setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                event.getEntityLiving().spawnAtLocation(mainhandItem);
            }
        }

    }

}

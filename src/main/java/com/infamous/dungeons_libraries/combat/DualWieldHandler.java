package com.infamous.dungeons_libraries.combat;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class DualWieldHandler {
    public static void switchHand(ServerPlayerEntity player) {
        if(!DungeonsLibrariesConfig.ENABLE_DUAL_WIELDING.get()) return;
        ItemStack mainHandItem = player.getMainHandItem();
        MeleeGearConfig config = MeleeGearConfigRegistry.getConfig(mainHandItem.getItem().getRegistryName());
        if(config.isLight()) {
            player.setItemInHand(Hand.MAIN_HAND, player.getOffhandItem());
            player.setItemInHand(Hand.OFF_HAND, mainHandItem);
        }
    }
}

package com.infamous.dungeons_libraries.combat;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class DualWieldHandler {
    public static void switchHand(ServerPlayer player) {
        if(!DungeonsLibrariesConfig.ENABLE_DUAL_WIELDING.get()) return;
        ItemStack mainHandItem = player.getMainHandItem();
        ItemStack offHandItem = player.getOffhandItem();
        MeleeGearConfig config = MeleeGearConfigRegistry.getConfig(mainHandItem.getItem().getRegistryName());
        if(config.isLight() && !offHandItem.isEmpty() && offHandItem.getItem() instanceof TieredItem) {
            player.setItemInHand(InteractionHand.MAIN_HAND, player.getOffhandItem());
            player.setItemInHand(InteractionHand.OFF_HAND, mainHandItem);
        }
    }
}

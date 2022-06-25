package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GearConfigEvents {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new GearConfigReload());
    }
}

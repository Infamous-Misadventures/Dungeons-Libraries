package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.items.interfaces.IReloadableGear;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class GearConfigReload implements ResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        reloadAll();
    }

    public static void reloadAll() {
        ITEMS.getEntries().stream().filter(registryKeyItemEntry -> registryKeyItemEntry.getValue() instanceof IReloadableGear).map(registryKeyItemEntry -> (IReloadableGear) registryKeyItemEntry.getValue()).forEach(IReloadableGear::reload);
    }

}
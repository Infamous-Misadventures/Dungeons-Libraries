package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class GearConfigReload implements IResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        ITEMS.getEntries().stream().filter(registryKeyItemEntry -> registryKeyItemEntry.getValue() instanceof MeleeGear).map(registryKeyItemEntry -> (MeleeGear) registryKeyItemEntry.getValue()).forEach(meleeGear -> meleeGear.reload());
    }

}
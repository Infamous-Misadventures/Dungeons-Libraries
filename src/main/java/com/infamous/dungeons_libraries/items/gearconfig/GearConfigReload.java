package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraft.client.resources.ReloadListener;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

public class GearConfigReload implements IResourceManagerReloadListener {

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        ITEMS.getEntries().stream().filter(registryKeyItemEntry -> registryKeyItemEntry.getValue() instanceof SwordGearConfig).map(registryKeyItemEntry -> (SwordGearConfig) registryKeyItemEntry.getValue()).forEach(swordGearConfig -> swordGearConfig.reload());
    }

}
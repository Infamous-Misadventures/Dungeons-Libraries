package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import com.infamous.dungeons_libraries.network.gearconfig.ArmorGearConfigSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class ArmorGearConfigRegistry {
    public static final ResourceLocation GEAR_CONFIG_BUILTIN_RESOURCELOCATION = new ResourceLocation(MODID, "gear_config");

    public static final CodecJsonDataManager<ArmorGearConfig> ARMOR_GEAR_CONFIGS = new CodecJsonDataManager<>("gearconfig/armor", ArmorGearConfig.CODEC, DungeonsLibraries.LOGGER);


    public static ArmorGearConfig getConfig(ResourceLocation resourceLocation) {
        return ARMOR_GEAR_CONFIGS.data.getOrDefault(resourceLocation, ArmorGearConfig.DEFAULT);
    }

    public static boolean gearConfigExists(ResourceLocation resourceLocation) {
        return ARMOR_GEAR_CONFIGS.data.containsKey(resourceLocation);
    }

    public static ArmorGearConfigSyncPacket toPacket(Map<ResourceLocation, ArmorGearConfig> map) {
        return new ArmorGearConfigSyncPacket(map);
    }
}
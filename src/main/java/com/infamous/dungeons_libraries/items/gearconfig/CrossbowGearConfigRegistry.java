package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import com.infamous.dungeons_libraries.network.gearconfig.CrossbowGearConfigSyncPacket;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class CrossbowGearConfigRegistry {
    public static final ResourceLocation GEAR_CONFIG_BUILTIN_RESOURCELOCATION = new ResourceLocation(MODID, "gear_config");

    public static final CodecJsonDataManager<BowGearConfig> CROSSBOW_GEAR_CONFIGS = new CodecJsonDataManager<>("gearconfig/crossbow", BowGearConfig.CODEC, DungeonsLibraries.LOGGER);


    public static BowGearConfig getConfig(ResourceLocation resourceLocation) {
        return CROSSBOW_GEAR_CONFIGS.data.getOrDefault(resourceLocation, BowGearConfig.DEFAULT);
    }

    public static boolean gearConfigExists(ResourceLocation resourceLocation) {
        return CROSSBOW_GEAR_CONFIGS.data.containsKey(resourceLocation);
    }

    public static CrossbowGearConfigSyncPacket toPacket(Map<ResourceLocation, BowGearConfig> map) {
        return new CrossbowGearConfigSyncPacket(map);
    }
}
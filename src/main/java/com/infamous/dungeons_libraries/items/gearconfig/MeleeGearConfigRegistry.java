package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import com.infamous.dungeons_libraries.network.gearconfig.MeleeGearConfigSyncPacket;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class MeleeGearConfigRegistry {
    public static final ResourceLocation GEAR_CONFIG_BUILTIN_RESOURCELOCATION = new ResourceLocation(MODID, "gear_config");

    public static final CodecJsonDataManager<MeleeGearConfig> MELEE_GEAR_CONFIGS = new CodecJsonDataManager<>("gearconfig/melee", MeleeGearConfig.CODEC);


    public static MeleeGearConfig getConfig(ResourceLocation resourceLocation) {
        return MELEE_GEAR_CONFIGS.getData().getOrDefault(resourceLocation, MeleeGearConfig.DEFAULT);
    }

    public static boolean gearConfigExists(ResourceLocation resourceLocation) {
        return MELEE_GEAR_CONFIGS.getData().containsKey(resourceLocation);
    }

    public static MeleeGearConfigSyncPacket toPacket(Map<ResourceLocation, MeleeGearConfig> map) {
        return new MeleeGearConfigSyncPacket(map);
    }
}
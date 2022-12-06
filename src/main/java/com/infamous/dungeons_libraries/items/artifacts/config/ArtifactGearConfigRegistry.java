package com.infamous.dungeons_libraries.items.artifacts.config;

import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import com.infamous.dungeons_libraries.network.gearconfig.ArtifactGearConfigSyncPacket;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class ArtifactGearConfigRegistry {
    public static final ResourceLocation GEAR_CONFIG_BUILTIN_RESOURCELOCATION = new ResourceLocation(MODID, "gear_config");

    public static final CodecJsonDataManager<ArtifactGearConfig> ARTIFACT_GEAR_CONFIGS = new CodecJsonDataManager<>("gearconfig/artifact", ArtifactGearConfig.CODEC);


    public static ArtifactGearConfig getConfig(ResourceLocation resourceLocation) {
        return ARTIFACT_GEAR_CONFIGS.getData().getOrDefault(resourceLocation, ArtifactGearConfig.DEFAULT);
    }

    public static boolean gearConfigExists(ResourceLocation resourceLocation) {
        return ARTIFACT_GEAR_CONFIGS.getData().containsKey(resourceLocation);
    }

    public static ArtifactGearConfigSyncPacket toPacket(Map<ResourceLocation, ArtifactGearConfig> map) {
        return new ArtifactGearConfigSyncPacket(map);
    }
}
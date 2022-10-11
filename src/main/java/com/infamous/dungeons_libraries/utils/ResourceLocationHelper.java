package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationHelper {
    public static ResourceLocation modLoc(String path) {
        return new ResourceLocation(DungeonsLibraries.MODID, path);
    }

    public static ResourceLocation forgeLoc(String path) {
        return new ResourceLocation("forge", path);
    }
}

package com.infamous.dungeons_libraries.data.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DefaultsCodecJsonDataManager<T> extends CodecJsonDataManager<T>{

    private Map<ResourceLocation, T> defaults = new HashMap<>();

    public DefaultsCodecJsonDataManager(String folderName, Codec<T> codec, Logger logger) {
        super(folderName, codec, logger);
    }

    public DefaultsCodecJsonDataManager(String folderName, Codec<T> codec, Logger logger, Gson gson) {
        super(folderName, codec, logger, gson);
    }

    public void addDefault(ResourceLocation resourceLocation, T object){
        defaults.put(resourceLocation, object);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        super.apply(jsons, resourceManager, profiler);
        defaults.forEach((resourceLocation, t) -> this.data.putIfAbsent(resourceLocation, t));
    }
}

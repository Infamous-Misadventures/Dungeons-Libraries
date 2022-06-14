package com.infamous.dungeons_libraries.summon;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SummonConfigRegistry {
    public static final ResourceLocation SUMMON_RESOURCELOCATION = new ResourceLocation(MODID, "summon");

    public static final CodecJsonDataManager<SummonConfig> SUMMON_CONFIGS = new CodecJsonDataManager<>("summon", SummonConfig.CODEC, DungeonsLibraries.LOGGER);


    public static SummonConfig getConfig(ResourceLocation resourceLocation) {
        return SUMMON_CONFIGS.data.getOrDefault(resourceLocation, SummonConfig.DEFAULT);
    }

    public static boolean gearConfigExists(ResourceLocation resourceLocation) {
        return SUMMON_CONFIGS.data.containsKey(resourceLocation);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(SUMMON_CONFIGS);
    }
}
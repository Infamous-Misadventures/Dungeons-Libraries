package com.infamous.dungeons_libraries.entities.elite;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.MergeableCodecDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EliteMobConfigRegistry {
    public static final MergeableCodecDataManager<EliteMobConfigList, List<EliteMobConfig>> ELITE_MOB_CONFIGS = new MergeableCodecDataManager<>("elite_mob", DungeonsLibraries.LOGGER, EliteMobConfigList.CODEC, EliteMobConfigRegistry::eliteMobMerger);

    public static List<EliteMobConfig> eliteMobMerger(List<EliteMobConfigList> raws){
        return raws.stream().flatMap(rawList -> rawList.getConfigs().stream()).collect(Collectors.toList());
    }

    public static EliteMobConfig getRandomConfig(ResourceLocation resourceLocation, Random random) {
        List<EliteMobConfig> eliteMobConfigs = ELITE_MOB_CONFIGS.data.getOrDefault(resourceLocation, Collections.emptyList());
        if(eliteMobConfigs.isEmpty()){
            return null;
        }
        return WeightedRandom.getRandomItem(random, eliteMobConfigs);
    }

    public static boolean eliteMobConfigExists(ResourceLocation resourceLocation) {
        return ELITE_MOB_CONFIGS.data.containsKey(resourceLocation);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ELITE_MOB_CONFIGS);
    }
}
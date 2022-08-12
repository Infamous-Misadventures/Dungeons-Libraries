package com.infamous.dungeons_libraries.entities.armored;

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
public class ArmoredMobConfigRegistry {
    public static final MergeableCodecDataManager<ArmoredMobConfigList, List<ArmoredMobConfig>> ARMORED_MOB_CONFIGS = new MergeableCodecDataManager<>("armored_mob", DungeonsLibraries.LOGGER, ArmoredMobConfigList.CODEC, ArmoredMobConfigRegistry::armoredMobMerger);

    public static List<ArmoredMobConfig> armoredMobMerger(List<ArmoredMobConfigList> raws){
        return raws.stream().flatMap(rawList -> rawList.getConfigs().stream()).collect(Collectors.toList());
    }

    public static ArmoredMobConfig getRandomConfig(ResourceLocation resourceLocation, Random random) {
        List<ArmoredMobConfig> armoredMobConfigs = ARMORED_MOB_CONFIGS.data.getOrDefault(resourceLocation, Collections.emptyList());
        if(armoredMobConfigs.isEmpty()){
            return null;
        }
        return WeightedRandom.getRandomItem(random, armoredMobConfigs);
    }

    public static boolean ArmoredMobConfigExists(ResourceLocation resourceLocation) {
        return ARMORED_MOB_CONFIGS.data.containsKey(resourceLocation);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ARMORED_MOB_CONFIGS);
    }
}
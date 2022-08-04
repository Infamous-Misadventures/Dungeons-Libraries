package com.infamous.dungeons_libraries.entities.armored;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.MergeableCodecDataManager;
import com.mojang.datafixers.util.Pair;
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
    public static final MergeableCodecDataManager<ArmoredMobConfig, List<ArmoredMobConfig>> ARMORED_MOB_CONFIGS = new MergeableCodecDataManager<>("armored_mob", DungeonsLibraries.LOGGER, ArmoredMobConfig.CODEC, ArmoredMobConfigRegistry::armoredMobMerger);

    public static List<ArmoredMobConfig> armoredMobMerger(List<ArmoredMobConfig> raws){
        return raws.stream().collect(Collectors.toList());
    }

    public static ArmoredMobConfig getRandomConfig(ResourceLocation resourceLocation, Random random) {
        return WeightedRandom.getRandomItem(random, ARMORED_MOB_CONFIGS.data.getOrDefault(resourceLocation, Collections.emptyList()));
    }

    public static boolean ArmoredMobConfigExists(ResourceLocation resourceLocation) {
        return ARMORED_MOB_CONFIGS.data.containsKey(resourceLocation);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(ARMORED_MOB_CONFIGS);
    }
}
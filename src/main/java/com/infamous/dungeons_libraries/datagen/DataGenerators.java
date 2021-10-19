package com.infamous.dungeons_libraries.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.item.Items.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeClient()) {
            generator.addProvider(new ModLanguageProvider(generator, "en_us"));
        }
        if (event.includeServer()) {
        }
    }
}
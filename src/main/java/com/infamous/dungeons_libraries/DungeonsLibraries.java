package com.infamous.dungeons_libraries;

import com.infamous.dungeons_libraries.capabilities.summoning.ISummonable;
import com.infamous.dungeons_libraries.capabilities.summoning.Summonable;
import com.infamous.dungeons_libraries.capabilities.summoning.SummonableStorage;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dungeons_libraries")
public class DungeonsLibraries
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dungeons_libraries";

    public DungeonsLibraries() {
        // Register the setup method for modloading
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsLibrariesConfig.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    private void setup(final FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(ISummonable.class, new SummonableStorage(), Summonable::new);
    }

}

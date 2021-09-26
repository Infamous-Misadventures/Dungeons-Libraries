package com.infamous.dungeons_libraries;

import com.infamous.dungeons_libraries.capabilities.cloneable.Cloneable;
import com.infamous.dungeons_libraries.capabilities.cloneable.CloneableStorage;
import com.infamous.dungeons_libraries.capabilities.cloneable.ICloneable;
import com.infamous.dungeons_libraries.capabilities.teamable.ITeamable;
import com.infamous.dungeons_libraries.capabilities.teamable.Teamable;
import com.infamous.dungeons_libraries.capabilities.teamable.TeamableStorage;
import com.infamous.dungeons_libraries.config.DungeonsMobsConfig;
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
import software.bernie.geckolib3.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dungeons_libraries")
public class DungeonsLibraries
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "dungeons_libraries";

    public DungeonsLibraries() {
        // Register the setup method for modloading
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, DungeonsMobsConfig.COMMON_SPEC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        GeckoLib.initialize();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    private void setup(final FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(ICloneable.class, new CloneableStorage(), Cloneable::new);
        CapabilityManager.INSTANCE.register(ITeamable.class, new TeamableStorage(), Teamable::new);
    }

}

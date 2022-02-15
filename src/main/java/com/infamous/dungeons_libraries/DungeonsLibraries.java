package com.infamous.dungeons_libraries;

import com.infamous.dungeons_libraries.attribute.AttributeRegistry;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsStorage;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.enchantable.Enchantable;
import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableStorage;
import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.capabilities.minionmaster.*;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.network.NetworkHandler;
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
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
    }

    private void setup(final FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(IMinion.class, new MinionStorage(), Minion::new);
        CapabilityManager.INSTANCE.register(IMaster.class, new MasterStorage(), Master::new);
        CapabilityManager.INSTANCE.register(IEnchantable.class, new EnchantableStorage(), Enchantable::new);
        CapabilityManager.INSTANCE.register(IBuiltInEnchantments.class, new BuiltInEnchantmentsStorage(), BuiltInEnchantments::new);
        event.enqueueWork(NetworkHandler::init);
    }

}

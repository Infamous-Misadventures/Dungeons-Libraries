package com.infamous.dungeons_libraries;

import com.infamous.dungeons_libraries.attribute.AttributeRegistry;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsStorage;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.IBuiltInEnchantments;
import com.infamous.dungeons_libraries.capabilities.enchantable.Enchantable;
import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableStorage;
import com.infamous.dungeons_libraries.capabilities.enchantable.IEnchantable;
import com.infamous.dungeons_libraries.capabilities.minionmaster.*;
import com.infamous.dungeons_libraries.capabilities.playerrewards.IPlayerRewards;
import com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewards;
import com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewardsStorage;
import com.infamous.dungeons_libraries.capabilities.soulcaster.ISoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterStorage;
import com.infamous.dungeons_libraries.capabilities.timers.ITimers;
import com.infamous.dungeons_libraries.capabilities.timers.Timers;
import com.infamous.dungeons_libraries.capabilities.timers.TimersStorage;
import com.infamous.dungeons_libraries.client.renderer.SoulOrbRenderer;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.entities.ModEntityTypes;
import com.infamous.dungeons_libraries.items.gearconfig.*;
import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterials;
import com.infamous.dungeons_libraries.items.materials.weapon.WeaponMaterials;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.infamous.dungeons_libraries.entities.ModEntityTypes.ENTITY_TYPES;
import static com.infamous.dungeons_libraries.items.gearconfig.ArmorGearConfigRegistry.ARMOR_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.gearconfig.BowGearConfigRegistry.BOW_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.gearconfig.CrossbowGearConfigRegistry.CROSSBOW_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.gearconfig.MeleeGearConfigRegistry.MELEE_GEAR_CONFIGS;
import static com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterials.ARMOR_MATERIALS;
import static com.infamous.dungeons_libraries.items.materials.weapon.WeaponMaterials.WEAPON_MATERIALS;

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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        ENTITY_TYPES.register(modEventBus);
        ArmorMaterials.setupVanillaMaterials();
        WeaponMaterials.setupVanillaMaterials();

        ARMOR_GEAR_CONFIGS.subscribeAsSyncable(NetworkHandler.INSTANCE, ArmorGearConfigRegistry::toPacket);
        MELEE_GEAR_CONFIGS.subscribeAsSyncable(NetworkHandler.INSTANCE, MeleeGearConfigRegistry::toPacket);
        BOW_GEAR_CONFIGS.subscribeAsSyncable(NetworkHandler.INSTANCE, BowGearConfigRegistry::toPacket);
        CROSSBOW_GEAR_CONFIGS.subscribeAsSyncable(NetworkHandler.INSTANCE, CrossbowGearConfigRegistry::toPacket);
        WEAPON_MATERIALS.subscribeAsSyncable(NetworkHandler.INSTANCE, WeaponMaterials::toPacket);
        ARMOR_MATERIALS.subscribeAsSyncable(NetworkHandler.INSTANCE, ArmorMaterials::toPacket);
    }

    private void setup(final FMLCommonSetupEvent event){
        CapabilityManager.INSTANCE.register(IMinion.class, new MinionStorage(), Minion::new);
        CapabilityManager.INSTANCE.register(IMaster.class, new MasterStorage(), Master::new);
        CapabilityManager.INSTANCE.register(IEnchantable.class, new EnchantableStorage(), Enchantable::new);
        CapabilityManager.INSTANCE.register(IBuiltInEnchantments.class, new BuiltInEnchantmentsStorage(), BuiltInEnchantments::new);
        CapabilityManager.INSTANCE.register(ISoulCaster.class, new SoulCasterStorage(), SoulCaster::new);
        CapabilityManager.INSTANCE.register(ITimers.class, new TimersStorage(), Timers::new);
        CapabilityManager.INSTANCE.register(IPlayerRewards.class, new PlayerRewardsStorage(), PlayerRewards::new);
        event.enqueueWork(NetworkHandler::init);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.SOUL_ORB.get(), SoulOrbRenderer::new);
    }

}

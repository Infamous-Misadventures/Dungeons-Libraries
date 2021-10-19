package com.infamous.dungeons_libraries.mobenchantments;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import static com.infamous.dungeons_libraries.DungeonsLibraries.LOGGER;
import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEnchantmentsRegistry {
    public static IForgeRegistry<MobEnchantment> MOB_ENCHANTMENTS = null;

    @SubscribeEvent
    public static void onNewRegistry(RegistryEvent.NewRegistry event){
        LOGGER.info("Starting creation of registry for Mob Enchantments");
        RegistryBuilder<MobEnchantment> registryBuilder = new RegistryBuilder<>();
        registryBuilder.setName(new ResourceLocation(MODID, "mob_enchantment"));
        registryBuilder.setType(MobEnchantment.class);
        registryBuilder.onAdd((owner, stage, id, obj, old) -> LOGGER.info("Registered Dungeons Libraries Resource: " + obj.getRegistryName()));
        MOB_ENCHANTMENTS = registryBuilder.create();
    }

}
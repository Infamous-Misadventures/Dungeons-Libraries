package com.infamous.dungeons_libraries.items.materials;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.CodecJsonDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorMaterials {

    public static final CodecJsonDataManager<ArmorMaterial> ARMOR_MATERIALS = new CodecJsonDataManager<>("material/armor", ArmorMaterial.CODEC, DungeonsLibraries.LOGGER);

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(ARMOR_MATERIALS);
    }

    public static ArmorMaterial getArmorMaterial(ResourceLocation resourceLocation){
        return ARMOR_MATERIALS.data.getOrDefault(resourceLocation, VanillaArmorMaterial.DEFAULT);
    }

    public static boolean ArmorMaterialExists(ResourceLocation boostResourceLocation){
        return ARMOR_MATERIALS.data.containsKey(boostResourceLocation);
    }

    public static Collection<ResourceLocation> armorMaterialsKeys(){
        return ARMOR_MATERIALS.data.keySet();
    }
}

package com.infamous.dungeons_libraries.items.materials.armor;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.DefaultsCodecJsonDataManager;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static net.minecraft.item.ArmorMaterial.*;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorMaterials {

    public static final DefaultsCodecJsonDataManager<IArmorMaterial> ARMOR_MATERIALS = new DefaultsCodecJsonDataManager<>("material/armor", DungeonsArmorMaterial.CODEC, DungeonsLibraries.LOGGER);

    public static void setupVanillaMaterials(){
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:leather"), LEATHER);
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:chainmail"), CHAIN);
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:iron"), IRON);
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:gold"), GOLD);
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:diamond"), DIAMOND);
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:turtle"), TURTLE);
        ARMOR_MATERIALS.addDefault(new ResourceLocation("minecraft:netherite"), NETHERITE);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event)
    {
        event.addListener(ARMOR_MATERIALS);
    }

    public static IArmorMaterial getArmorMaterial(ResourceLocation resourceLocation){
        return ARMOR_MATERIALS.data.getOrDefault(resourceLocation, IRON);
    }

    public static boolean ArmorMaterialExists(ResourceLocation resourceLocation){
        return ARMOR_MATERIALS.data.containsKey(resourceLocation);
    }

    public static Collection<ResourceLocation> armorMaterialsKeys(){
        return ARMOR_MATERIALS.data.keySet();
    }
}
package com.infamous.dungeons_libraries.items.materials.weapon;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.data.util.DefaultsCodecJsonDataManager;
import com.infamous.dungeons_libraries.network.materials.WeaponMaterialSyncPacket;
import net.minecraft.item.IItemTier;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Map;

import static net.minecraft.item.ItemTier.*;

public class WeaponMaterials {

    public static final DefaultsCodecJsonDataManager<IItemTier> WEAPON_MATERIALS = new DefaultsCodecJsonDataManager<>("material/weapon", DungeonsWeaponMaterial.CODEC, DungeonsLibraries.LOGGER);

    public static void setupVanillaMaterials(){
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:wood"), WOOD);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:stone"), STONE);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:iron"), IRON);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:diamond"), DIAMOND);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:gold"), GOLD);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:netherite"), NETHERITE);
    }

    public static IItemTier getWeaponMaterial(ResourceLocation resourceLocation){
        return WEAPON_MATERIALS.data.getOrDefault(resourceLocation, IRON);
    }

    public static boolean WeaponMaterialExists(ResourceLocation boostResourceLocation){
        return WEAPON_MATERIALS.data.containsKey(boostResourceLocation);
    }

    public static Collection<ResourceLocation> weaponMaterialsKeys(){
        return WEAPON_MATERIALS.data.keySet();
    }

    public static WeaponMaterialSyncPacket toPacket(Map<ResourceLocation, IItemTier> map){
        return new WeaponMaterialSyncPacket(map);
    }
}
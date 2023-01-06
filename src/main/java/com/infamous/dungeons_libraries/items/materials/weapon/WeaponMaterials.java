package com.infamous.dungeons_libraries.items.materials.weapon;

import com.infamous.dungeons_libraries.data.util.DefaultsCodecJsonDataManager;
import com.infamous.dungeons_libraries.network.materials.WeaponMaterialSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;

import java.util.Collection;
import java.util.Map;

import static net.minecraft.world.item.Tiers.*;

public class WeaponMaterials {

    public static final DefaultsCodecJsonDataManager<Tier> WEAPON_MATERIALS = new DefaultsCodecJsonDataManager<>("material/weapon", DungeonsWeaponMaterial.CODEC);

    public static void setupVanillaMaterials() {
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:wood"), WOOD);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:stone"), STONE);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:iron"), IRON);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:diamond"), DIAMOND);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:gold"), GOLD);
        WEAPON_MATERIALS.addDefault(new ResourceLocation("minecraft:netherite"), NETHERITE);
    }

    public static Tier getWeaponMaterial(ResourceLocation resourceLocation) {
        return WEAPON_MATERIALS.getData().getOrDefault(resourceLocation, IRON);
    }

    public static boolean WeaponMaterialExists(ResourceLocation boostResourceLocation) {
        return WEAPON_MATERIALS.getData().containsKey(boostResourceLocation);
    }

    public static Collection<ResourceLocation> weaponMaterialsKeys() {
        return WEAPON_MATERIALS.getData().keySet();
    }

    public static WeaponMaterialSyncPacket toPacket(Map<ResourceLocation, Tier> map) {
        return new WeaponMaterialSyncPacket(map);
    }
}
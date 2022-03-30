package com.infamous.dungeons_libraries.items.materials;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.items.materials.ArmorMaterialTypeRegistry.ARMOR_MATERIAL_DISPATCHER;

public class ArmorMaterialTypes {
    public static final DeferredRegister<ArmorMaterialTypeRegistry.Serializer<?>> ARMOR_MATERIAL_TYPES = ARMOR_MATERIAL_DISPATCHER.makeDeferredRegister(MODID);

    public static final RegistryObject<ArmorMaterialTypeRegistry.Serializer<DungeonsArmorMaterial>> DUNGEONS = ARMOR_MATERIAL_TYPES.register("dungeons_armor_material", () -> new ArmorMaterialTypeRegistry.Serializer<>(DungeonsArmorMaterial.CODEC));
    public static final RegistryObject<ArmorMaterialTypeRegistry.Serializer<VanillaArmorMaterial>> VANILLA = ARMOR_MATERIAL_TYPES.register("vanilla_armor_material", () -> new ArmorMaterialTypeRegistry.Serializer<>(VanillaArmorMaterial.CODEC));

}

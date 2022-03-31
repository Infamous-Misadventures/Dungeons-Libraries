package com.infamous.dungeons_libraries.items.gearconfig.client;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.data.BlockStateVariantBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class ArmorGearModels {
    private static final HashMap<ResourceLocation, BlockStateVariantBuilder.ITriFunction<Float, EquipmentSlotType, LivingEntity, BipedModel>> ARMOR_GEAR_MODELS = new HashMap<>();

    public static void addModel(ResourceLocation resourceLocation, BlockStateVariantBuilder.ITriFunction<Float, EquipmentSlotType, LivingEntity, BipedModel> modelSupplier){
        ARMOR_GEAR_MODELS.put(resourceLocation, modelSupplier);
    }

    public static BlockStateVariantBuilder.ITriFunction<Float, EquipmentSlotType, LivingEntity, BipedModel> getModel(ResourceLocation resourceLocation){
        return ARMOR_GEAR_MODELS.get(resourceLocation);
    }

}

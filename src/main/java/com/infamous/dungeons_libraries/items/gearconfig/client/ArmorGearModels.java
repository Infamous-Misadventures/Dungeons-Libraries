package com.infamous.dungeons_libraries.items.gearconfig.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;

public class ArmorGearModels {
    private static final HashMap<ResourceLocation, PropertyDispatch.TriFunction<Float, EquipmentSlot, LivingEntity, HumanoidModel>> ARMOR_GEAR_MODELS = new HashMap<>();

    public static void addModel(ResourceLocation resourceLocation, PropertyDispatch.TriFunction<Float, EquipmentSlot, LivingEntity, HumanoidModel> modelSupplier){
        ARMOR_GEAR_MODELS.put(resourceLocation, modelSupplier);
    }

    public static PropertyDispatch.TriFunction<Float, EquipmentSlot, LivingEntity, HumanoidModel> getModel(ResourceLocation resourceLocation){
        return ARMOR_GEAR_MODELS.get(resourceLocation);
    }

}

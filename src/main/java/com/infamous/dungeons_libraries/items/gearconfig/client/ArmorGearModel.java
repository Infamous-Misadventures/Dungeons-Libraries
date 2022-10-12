package com.infamous.dungeons_libraries.items.gearconfig.client;

import com.infamous.dungeons_libraries.items.gearconfig.ArmorGear;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ArmorGearModel<T extends ArmorGear> extends AnimatedGeoModel<T> {
    @Override
    public ResourceLocation getModelLocation(ArmorGear object) {
        return object.getModelLocation();
    }

    @Override
    public ResourceLocation getTextureLocation(ArmorGear object) {
        return object.getTextureLocation();
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ArmorGear object) {
        return object.getAnimationFileLocation();
    }
}

package com.infamous.dungeons_libraries.mixin;

import net.minecraft.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorMaterial.class)
public interface ArmorMaterialAccessor {

    @Accessor
    String getName();
}

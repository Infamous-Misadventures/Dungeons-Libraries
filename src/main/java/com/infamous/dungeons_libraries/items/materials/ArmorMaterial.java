package com.infamous.dungeons_libraries.items.materials;

import com.infamous.dungeons_libraries.data.util.RegistryDispatcher;
import com.mojang.serialization.Codec;
import net.minecraft.item.IArmorMaterial;

import java.util.function.Supplier;

public abstract class ArmorMaterial extends RegistryDispatcher.Dispatchable<ArmorMaterialTypeRegistry.Serializer<?>> implements IArmorMaterial {

    public static final Codec<ArmorMaterial> CODEC = ArmorMaterialTypeRegistry.ARMOR_MATERIAL_DISPATCHER.getDispatchedCodec();

    public ArmorMaterial(Supplier<? extends ArmorMaterialTypeRegistry.Serializer<?>> dispatcherGetter) {
        super(dispatcherGetter);
    }
}

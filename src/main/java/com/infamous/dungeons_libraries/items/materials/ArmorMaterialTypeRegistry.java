package com.infamous.dungeons_libraries.items.materials;

import com.infamous.dungeons_libraries.data.util.RegistryDispatcher;
import com.mojang.serialization.Codec;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class ArmorMaterialTypeRegistry {
    public static RegistryDispatcher<Serializer<?>, ArmorMaterial> ARMOR_MATERIAL_DISPATCHER;
    public static void setup(IEventBus modEventBus){
        ARMOR_MATERIAL_DISPATCHER = RegistryDispatcher.makeDispatchForgeRegistry(
                modEventBus,
                IArmorMaterial.class,
                new ResourceLocation(MODID, "armor_material_type"),
                builder -> builder
                        .disableSaving()
                        .disableSync()
        );
    }
    public static class Serializer<P extends ArmorMaterial> extends RegistryDispatcher.Dispatcher<Serializer<?>, P>
    {
        public Serializer(Codec<P> subCodec)
        {
            super(subCodec);
        }
    }
}

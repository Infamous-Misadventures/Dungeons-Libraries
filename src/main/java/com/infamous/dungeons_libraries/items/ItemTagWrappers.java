package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ItemTagWrappers {

    public static final TagKey<Item> CURIOS_ARTIFACTS = tag(new ResourceLocation("curios", "artifact"));

    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(DungeonsLibraries.MODID, name));
    }

    private static TagKey<Item> tag(ResourceLocation resourceLocation) {
        return TagKey.create(Registry.ITEM_REGISTRY, resourceLocation);
    }
}

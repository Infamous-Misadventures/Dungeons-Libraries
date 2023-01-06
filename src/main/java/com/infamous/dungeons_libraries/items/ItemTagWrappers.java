package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.infamous.dungeons_libraries.utils.ResourceLocationHelper.modLoc;

public class ItemTagWrappers {

    public static final TagKey<Item> CURIOS_ARTIFACTS = tag(new ResourceLocation("curios", "artifact"));
    public static final TagKey<Item> ARTIFACT_REPAIR_ITEMS = tag(modLoc("artifact_repair_items"));

    private static TagKey<Item> tag(String name) {
        return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(DungeonsLibraries.MODID, name));
    }

    private static TagKey<Item> tag(ResourceLocation resourceLocation) {
        return TagKey.create(Registry.ITEM_REGISTRY, resourceLocation);
    }

    public static void init() {
    }
}

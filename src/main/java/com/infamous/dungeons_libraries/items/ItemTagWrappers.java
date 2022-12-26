package com.infamous.dungeons_libraries.items;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ItemTagWrappers {

    public static final Tags.IOptionalNamedTag<Item> CURIOS_ARTIFACTS = ItemTags.createOptional(new ResourceLocation("curios", "artifact"));
    public static final Tags.IOptionalNamedTag<Item> ARTIFACT_REPAIR_ITEMS = ItemTags.createOptional(new ResourceLocation(DungeonsLibraries.MODID, "artifact_repair_items"));

    public static void init(){}
}

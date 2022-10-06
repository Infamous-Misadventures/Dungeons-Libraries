package com.infamous.dungeons_libraries.tags;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ItemTags {
    public static final Tags.IOptionalNamedTag<Item> ARTIFACT_REPAIR_ITEMS = net.minecraft.tags.ItemTags.createOptional(new ResourceLocation(DungeonsLibraries.MODID, "artifact_repair_items"));
}

package com.infamous.dungeons_libraries.tags;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.infamous.dungeons_libraries.utils.ResourceLocationHelper.modLoc;

public class ItemTags {

    public static final TagKey<Item> ARTIFACT_REPAIR_ITEMS = net.minecraft.tags.ItemTags.create(modLoc("artifact_repair_items"));
}

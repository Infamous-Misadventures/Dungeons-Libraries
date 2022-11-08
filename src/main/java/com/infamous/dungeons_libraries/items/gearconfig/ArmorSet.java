package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

public class ArmorSet {
    private final ResourceLocation registryName;
    private final RegistryObject<Item> head;
    private final RegistryObject<Item> chest;
    private final RegistryObject<Item> legs;
    private final RegistryObject<Item> feet;

    public ArmorSet(ResourceLocation registryName, RegistryObject<Item> head, RegistryObject<Item> chest, RegistryObject<Item> legs, RegistryObject<Item> feet) {
        this.registryName = registryName;
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.feet = feet;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public RegistryObject<Item> getHead() {
        return head;
    }

    public RegistryObject<Item> getChest() {
        return chest;
    }

    public RegistryObject<Item> getLegs() {
        return legs;
    }

    public RegistryObject<Item> getFeet() {
        return feet;
    }
}
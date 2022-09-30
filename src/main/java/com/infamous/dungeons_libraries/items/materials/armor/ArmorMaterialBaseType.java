package com.infamous.dungeons_libraries.items.materials.armor;


import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

public enum ArmorMaterialBaseType {
    CLOTH("cloth"),
    BONE("bone"),
    LEATHER("leather"),
    METAL("metal"),
    GEM("gem"),
    WOOD("wood"),
    UNKNOWN("unknown");

    public static final Codec<ArmorMaterialBaseType> CODEC = Codec.STRING.flatComapMap(s -> ArmorMaterialBaseType.byName(s, null), d -> DataResult.success(d.getName()));

    private final String name;

    ArmorMaterialBaseType(String name) {
        this.name = name;
    }

    public static ArmorMaterialBaseType byName(String name, ArmorMaterialBaseType defaultRank) {
        for (ArmorMaterialBaseType factionRank : values()) {
            if (factionRank.name.equals(name)) {
                return factionRank;
            }
        }
        return defaultRank;
    }

    public String getName() {
        return name;
    }
}
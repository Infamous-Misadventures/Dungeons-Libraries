package com.infamous.dungeons_libraries.entities.armored;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmoredMobConfigList {

    public static final ArmoredMobConfigList DEFAULT = new ArmoredMobConfigList(new ArrayList<>());

    public static final Codec<ArmoredMobConfigList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ArmoredMobConfig.CODEC.listOf().fieldOf("configs").forGetter(armoredMobConfigList -> armoredMobConfigList.configs)
    ).apply(instance, ArmoredMobConfigList::new));

    private final List<ArmoredMobConfig> configs;

    public ArmoredMobConfigList(List<ArmoredMobConfig> configs) {
        this.configs = configs;
    }

    public List<ArmoredMobConfig> getConfigs() {
        return configs;
    }
}

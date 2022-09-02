package com.infamous.dungeons_libraries.entities.elite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class EliteMobConfigList {

    public static final EliteMobConfigList DEFAULT = new EliteMobConfigList(new ArrayList<>());

    public static final Codec<EliteMobConfigList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            EliteMobConfig.CODEC.listOf().fieldOf("configs").forGetter(eliteMobConfigList -> eliteMobConfigList.configs)
    ).apply(instance, EliteMobConfigList::new));

    private final List<EliteMobConfig> configs;

    public EliteMobConfigList(List<EliteMobConfig> configs) {
        this.configs = configs;
    }

    public List<EliteMobConfig> getConfigs() {
        return configs;
    }
}

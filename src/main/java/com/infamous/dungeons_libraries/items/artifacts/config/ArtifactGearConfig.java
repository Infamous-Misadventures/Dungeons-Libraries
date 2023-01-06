package com.infamous.dungeons_libraries.items.artifacts.config;

import com.infamous.dungeons_libraries.items.gearconfig.GearConfigAttributeModifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;

public class ArtifactGearConfig {

    public static final ArtifactGearConfig DEFAULT = new ArtifactGearConfig(new ArrayList<>(), 20, 64, 5);

    public static final Codec<ArtifactGearConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            GearConfigAttributeModifier.CODEC.listOf().optionalFieldOf("attributes", new ArrayList<>()).forGetter(ArtifactGearConfig::getAttributes),
            Codec.INT.optionalFieldOf("cooldown", 20).forGetter(ArtifactGearConfig::getCooldown),
            Codec.INT.optionalFieldOf("durability", 64).forGetter(ArtifactGearConfig::getDurability),
            Codec.INT.optionalFieldOf("duration", 5).forGetter(ArtifactGearConfig::getDuration)
    ).apply(instance, ArtifactGearConfig::new));

    private final List<GearConfigAttributeModifier> attributes;
    private final int cooldown;
    private final int durability;
    private final int duration;

    public ArtifactGearConfig(List<GearConfigAttributeModifier> attributes, int cooldown, int durability, int duration) {
        this.attributes = attributes;
        this.cooldown = cooldown;
        this.durability = durability;
        this.duration = duration;
    }

    public List<GearConfigAttributeModifier> getAttributes() {
        return attributes;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getDurability() {
        return durability;
    }

    public int getDuration() {
        return duration;
    }
}
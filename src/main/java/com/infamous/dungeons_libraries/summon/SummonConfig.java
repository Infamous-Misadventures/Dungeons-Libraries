package com.infamous.dungeons_libraries.summon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class SummonConfig {
    
    public static final SummonConfig DEFAULT = new SummonConfig(1, false);

    public static final Codec<SummonConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("cost").forGetter(summonConfig -> summonConfig.cost),
            Codec.BOOL.optionalFieldOf("add_attack_goal", false).forGetter(summonConfig -> summonConfig.addAttackGoal)
    ).apply(instance, SummonConfig::new));

    private int cost;
    private boolean addAttackGoal;

    public SummonConfig(int cost, boolean addAttackGoal) {
        this.cost = cost;
        this.addAttackGoal = addAttackGoal;
    }

    public int getCost() {
        return cost;
    }

    public boolean shouldAddAttackGoal() {
        return addAttackGoal;
    }
}

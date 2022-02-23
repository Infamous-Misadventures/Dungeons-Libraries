package com.infamous.dungeons_libraries.capabilities.soulcaster;


import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public interface ISoulCaster {
    void addSouls(float amount, LivingEntity living);
    void setSouls(float amount, @Nullable LivingEntity living);

    float getSouls();
}

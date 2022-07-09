package com.infamous.dungeons_libraries.mixin;

import net.minecraft.entity.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEntity.class)
public interface MobEntityInvoker {

    @Invoker("registerGoals")
    void registerGoals();
}

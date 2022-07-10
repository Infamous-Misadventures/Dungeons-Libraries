package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface MobInvoker {

    @Invoker("registerGoals")
    void invokeRegisterGoals();
}

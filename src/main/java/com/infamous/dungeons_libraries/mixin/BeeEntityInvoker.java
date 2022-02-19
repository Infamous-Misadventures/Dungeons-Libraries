package com.infamous.dungeons_libraries.mixin;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.class)
public interface BeeEntityInvoker {

    @Invoker("setHasStung")
    void dungeons_gear_setHasStung(boolean p_226449_1_);
}

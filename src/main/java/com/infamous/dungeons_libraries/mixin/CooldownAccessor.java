package com.infamous.dungeons_libraries.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.world.item.ItemCooldowns.CooldownInstance")
public interface CooldownAccessor {

    @Accessor
    int getStartTime();

    @Accessor
    int getEndTime();
}

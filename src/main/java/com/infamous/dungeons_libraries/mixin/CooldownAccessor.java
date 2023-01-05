package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.item.ItemCooldowns;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCooldowns.CooldownInstance.class)
public interface CooldownAccessor {

    @Accessor
    int getStartTime();

    @Accessor
    int getEndTime();
}

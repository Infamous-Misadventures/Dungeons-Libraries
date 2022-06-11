package com.infamous.dungeons_libraries.mixin;

import net.minecraft.item.IItemTier;
import net.minecraft.item.TieredItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TieredItem.class)
public interface TieredItemAccessor {

    @Accessor
    void setTier(IItemTier itemTier);
}

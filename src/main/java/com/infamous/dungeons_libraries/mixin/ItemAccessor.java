package com.infamous.dungeons_libraries.mixin;

import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(Item.class)
public interface ItemAccessor {

    @Accessor
    void setMaxDamage(int maxDamage);

    @Accessor
    Map<ToolType, Integer> getToolClasses();
}

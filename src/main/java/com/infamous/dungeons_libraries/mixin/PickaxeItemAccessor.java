package com.infamous.dungeons_libraries.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;
import net.minecraft.item.PickaxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(PickaxeItem.class)
public interface PickaxeItemAccessor {

    @Accessor
    static Set<Block> getDIGGABLES() { throw new RuntimeException("Accessor failed to mixin"); }
}

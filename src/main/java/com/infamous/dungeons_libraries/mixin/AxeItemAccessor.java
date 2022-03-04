package com.infamous.dungeons_libraries.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {

    @Accessor
    static Set<Block> getOTHER_DIGGABLE_BLOCKS() { throw new RuntimeException("Accessor failed to mixin"); }

    @Accessor
    static Map<Block, Block> getSTRIPABLES() { throw new RuntimeException("Accessor failed to mixin"); }

    @Accessor
    static Set<Material> getDIGGABLE_MATERIALS() { throw new RuntimeException("Accessor failed to mixin"); }
}

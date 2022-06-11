package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ToolGear extends MeleeGear {
    /** Hardcoded set of blocks this tool can properly dig at full speed. Modders see instead. */
    private final Set<Block> diggableBlocks;

    public ToolGear(Set<Block> diggableBlocks, Properties properties) {
        super(properties);
        this.diggableBlocks = diggableBlocks;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (getToolTypes(pStack).stream().anyMatch(e -> pState.isToolEffective(e))) return this.getTier().getSpeed();
        return this.diggableBlocks.contains(pState.getBlock()) ? this.getTier().getSpeed() : 1.0F;
    }

}

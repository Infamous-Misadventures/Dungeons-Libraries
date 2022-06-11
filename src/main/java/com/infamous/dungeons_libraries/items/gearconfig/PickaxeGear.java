package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.mixin.PickaxeItemAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;

public class PickaxeGear extends ToolGear {

    public PickaxeGear(Properties properties) {
        super(PickaxeItemAccessor.getDIGGABLES(), properties.addToolType(ToolType.PICKAXE, 0));
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState pBlock) {
        int i = this.getTier().getLevel();
        if (pBlock.getHarvestTool() == net.minecraftforge.common.ToolType.PICKAXE) {
            return i >= pBlock.getHarvestLevel();
        }
        Material material = pBlock.getMaterial();
        return material == Material.STONE || material == Material.METAL || material == Material.HEAVY_METAL;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        Material material = pState.getMaterial();
        return material != Material.METAL && material != Material.HEAVY_METAL && material != Material.STONE ? super.getDestroySpeed(pStack, pState) : this.getTier().getSpeed();
    }
}

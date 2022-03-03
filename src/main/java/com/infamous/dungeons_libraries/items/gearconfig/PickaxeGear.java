package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.mixin.AxeItemAccessor;
import com.infamous.dungeons_libraries.mixin.PickaxeItemAccessor;
import com.infamous.dungeons_libraries.utils.MojankHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public class PickaxeGear extends ToolGear {

    public PickaxeGear(IItemTier tier, Properties properties) {
        super(tier, PickaxeItemAccessor.getDIGGABLES(), properties.addToolType(ToolType.PICKAXE, tier.getLevel()));
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

package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.mixin.AxeItemAccessor;
import com.infamous.dungeons_libraries.utils.MojankHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
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

import java.util.Set;

public class AxeGear extends ToolGear {

    public AxeGear(IItemTier tier, Properties properties) {
        super(tier, AxeItemAccessor.getOTHER_DIGGABLE_BLOCKS(), properties.addToolType(net.minecraftforge.common.ToolType.AXE, tier.getLevel()));
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        Material material = pState.getMaterial();
        return AxeItemAccessor.getDIGGABLE_MATERIALS().contains(material) ? this.getTier().getSpeed() : super.getDestroySpeed(pStack, pState);
    }

    /**
     * Called when this item is used when targetting a Block
     */
    public ActionResultType useOn(ItemUseContext pContext) {
        World world = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        BlockState block = blockstate.getToolModifiedState(world, blockpos, pContext.getPlayer(), pContext.getItemInHand(), net.minecraftforge.common.ToolType.AXE);
        if (block != null) {
            PlayerEntity playerentity = pContext.getPlayer();
            world.playSound(playerentity, blockpos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!world.isClientSide) {
                world.setBlock(blockpos, block, 11);
                if (playerentity != null) {
                    pContext.getItemInHand().hurtAndBreak(1, playerentity, MojankHelper::hurtEnemyBroadcastBreakEvent);
                }
            }
            return ActionResultType.sidedSuccess(world.isClientSide);
        } else {
            return ActionResultType.PASS;
        }
    }
}

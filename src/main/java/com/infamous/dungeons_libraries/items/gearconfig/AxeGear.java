package com.infamous.dungeons_libraries.items.gearconfig;

import com.infamous.dungeons_libraries.mixin.AxeItemAccessor;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

import net.minecraft.world.item.Item.Properties;

public class AxeGear extends ToolGear {

    public AxeGear(Properties properties) {
        super(BlockTags.MINEABLE_WITH_AXE, properties);
    }

    public InteractionResult useOn(UseOnContext p_40529_) {
        Level level = p_40529_.getLevel();
        BlockPos blockpos = p_40529_.getClickedPos();
        Player player = p_40529_.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(p_40529_, net.minecraftforge.common.ToolActions.AXE_STRIP, false));
        Optional<BlockState> optional1 = optional.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(p_40529_, net.minecraftforge.common.ToolActions.AXE_SCRAPE, false));
        Optional<BlockState> optional2 = optional.isPresent() || optional1.isPresent() ? Optional.empty() : Optional.ofNullable(blockstate.getToolModifiedState(p_40529_, net.minecraftforge.common.ToolActions.AXE_WAX_OFF, false));
        ItemStack itemstack = p_40529_.getItemInHand();
        Optional<BlockState> optional3 = Optional.empty();
        if (optional.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            optional3 = optional;
        } else if (optional1.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3005, blockpos, 0);
            optional3 = optional1;
        } else if (optional2.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.levelEvent(player, 3004, blockpos, 0);
            optional3 = optional2;
        }

        if (optional3.isPresent()) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }

            level.setBlock(blockpos, optional3.get(), 11);
            if (player != null) {
                itemstack.hurtAndBreak(1, player, (p_150686_) -> {
                    p_150686_.broadcastBreakEvent(p_40529_.getHand());
                });
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @javax.annotation.Nullable
    public static BlockState getAxeStrippingState(BlockState originalState) {
        Block block = AxeItemAccessor.getSTRIPPABLES().get(originalState.getBlock());
        return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, originalState.getValue(RotatedPillarBlock.AXIS)) : null;
    }

    private Optional<BlockState> getStripped(BlockState p_150691_) {
        return Optional.ofNullable(AxeItemAccessor.getSTRIPPABLES().get(p_150691_.getBlock())).map((p_150689_) -> {
            return p_150689_.defaultBlockState().setValue(RotatedPillarBlock.AXIS, p_150691_.getValue(RotatedPillarBlock.AXIS));
        });
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
    }
}

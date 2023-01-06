package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ToolGear extends MeleeGear {
    private final TagKey<Block> blocks;

    public ToolGear(TagKey<Block> blocks, Properties properties) {
        super(properties);
        this.blocks = blocks;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return pState.is(this.blocks) ? this.getTier().getSpeed() : 1.0F;
    }

    @Deprecated // FORGE: Use stack sensitive variant below
    @Override
    public boolean isCorrectToolForDrops(BlockState p_150816_) {
        if (net.minecraftforge.common.TierSortingRegistry.isTierSorted(getTier())) {
            return net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), p_150816_) && p_150816_.is(this.blocks);
        }
        int i = this.getTier().getLevel();
        if (i < 3 && p_150816_.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if (i < 2 && p_150816_.is(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return (i >= 1 || !p_150816_.is(BlockTags.NEEDS_STONE_TOOL)) && p_150816_.is(this.blocks);
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.category == EnchantmentCategory.DIGGER;
    }

    // FORGE START
    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.is(blocks) && net.minecraftforge.common.TierSortingRegistry.isCorrectTierForDrops(getTier(), state);
    }

}

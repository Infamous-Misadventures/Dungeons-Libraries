package com.infamous.dungeons_libraries.items.gearconfig;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;

public class PickaxeGear extends ToolGear {

    public PickaxeGear(Properties properties) {
        super(BlockTags.MINEABLE_WITH_PICKAXE, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }
}

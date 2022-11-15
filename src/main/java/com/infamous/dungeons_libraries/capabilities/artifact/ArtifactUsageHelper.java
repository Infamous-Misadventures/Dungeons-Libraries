package com.infamous.dungeons_libraries.capabilities.artifact;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.ARTIFACT_USAGE_CAPABILITY;


public class ArtifactUsageHelper {

    public static ArtifactUsage getArtifactUsageCapability(Entity entity)
    {
        return entity.getCapability(ARTIFACT_USAGE_CAPABILITY).orElse(new ArtifactUsage());
    }

    public static boolean startUsingArtifact(Player playerIn, ArtifactUsage cap, ItemStack itemstack){
        boolean result = cap.startUsingArtifact(itemstack);
        return result;
    }
}

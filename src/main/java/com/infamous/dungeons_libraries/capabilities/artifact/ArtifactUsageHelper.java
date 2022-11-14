package com.infamous.dungeons_libraries.capabilities.artifact;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

import static com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageProvider.ARTIFACT_USAGE_CAPABILITY;


public class ArtifactUsageHelper {

    public static IArtifactUsage getArtifactUsageCapability(Entity entity)
    {
        LazyOptional<IArtifactUsage> lazyCap = entity.getCapability(ARTIFACT_USAGE_CAPABILITY);
        return lazyCap.orElse(new ArtifactUsage());
    }

    public static boolean startUsingArtifact(PlayerEntity playerIn, IArtifactUsage cap, ItemStack itemstack){
        boolean result = cap.startUsingArtifact(itemstack);
        return result;
    }
}

package com.infamous.dungeons_libraries.network.client;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.capabilities.artifact.IArtifactUsage;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_libraries.integration.curios.client.message.CuriosArtifactStopMessage;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.infamous.dungeons_libraries.network.message.EliteMobMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientHandler {

    public static void handleCuriosArtifactStopMessage(CuriosArtifactStopMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        if (message != null) {
            NetworkEvent.Context context = contextSupplier.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                context.enqueueWork(() -> {
                    ClientPlayerEntity player = Minecraft.getInstance().player;
                    if (player != null) {
                        IArtifactUsage cap = ArtifactUsageHelper.getArtifactUsageCapability(player);
                        if (cap != null) {
                            ItemStack artifactStack = cap.getUsingArtifact();
                            if (artifactStack != null && artifactStack.getItem() instanceof ArtifactItem) {
                                ArtifactItem artifactItem = (ArtifactItem) artifactStack.getItem();
                                artifactItem.stopUsingArtifact(player);
                                cap.stopUsingArtifact();
                            }
                        }
                    }
                });
            }
        }
    }

    public static void handleEliteMobMessage(EliteMobMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.getEntityId());
                if (entity instanceof LivingEntity) {
                    EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
                    if(cap == null) return;
                    cap.setElite(message.isElite());
                    cap.setTexture(message.getTexture());
                    if(cap.isElite()) {
                        entity.refreshDimensions();
                    }
                }
            });
        }
    }
}

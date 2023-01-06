package com.infamous.dungeons_libraries.network.client;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsage;
import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import com.infamous.dungeons_libraries.integration.curios.client.message.CuriosArtifactStopMessage;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import com.infamous.dungeons_libraries.network.EliteMobMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientHandler {
    public static void handleCuriosArtifactStopMessage(CuriosArtifactStopMessage packet, Supplier<NetworkEvent.Context> ctx) {
        if (packet != null) {
            NetworkEvent.Context context = ctx.get();
            if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                context.enqueueWork(() -> {
                    AbstractClientPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        ArtifactUsage cap = ArtifactUsageHelper.getArtifactUsageCapability(player);
                        ItemStack artifactStack = cap.getUsingArtifact();
                        if (artifactStack != null && artifactStack.getItem() instanceof ArtifactItem artifactItem) {
                            artifactItem.stopUsingArtifact(player);
                            cap.stopUsingArtifact();
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
                    cap.setElite(message.isElite());
                    cap.setTexture(message.getTexture());
                    if (cap.isElite()) {
                        entity.refreshDimensions();
                    }
                }
            });
        }
    }
}

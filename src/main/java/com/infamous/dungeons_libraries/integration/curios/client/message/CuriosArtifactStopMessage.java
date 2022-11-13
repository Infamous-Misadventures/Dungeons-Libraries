package com.infamous.dungeons_libraries.integration.curios.client.message;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.capabilities.artifact.IArtifactUsage;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CuriosArtifactStopMessage {

    public CuriosArtifactStopMessage() {
    }

    public static void encode(CuriosArtifactStopMessage packet, PacketBuffer buf) {

    }

    public static CuriosArtifactStopMessage decode(PacketBuffer buf) {
        return new CuriosArtifactStopMessage();
    }

    public static class CuriosArtifactHandler {
        public static void handle(CuriosArtifactStopMessage packet, Supplier<NetworkEvent.Context> ctx) {
            if (packet != null) {
                ctx.get().setPacketHandled(true);
                ctx.get().enqueueWork(() -> {
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
}
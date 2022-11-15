package com.infamous.dungeons_libraries.integration.curios.client.message;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsage;
import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CuriosArtifactStopMessage {

    public CuriosArtifactStopMessage() {
    }

    public static void encode(CuriosArtifactStopMessage packet, FriendlyByteBuf buf) {

    }

    public static CuriosArtifactStopMessage decode(FriendlyByteBuf buf) {
        return new CuriosArtifactStopMessage();
    }

    public static class CuriosArtifactHandler {
        public static void handle(CuriosArtifactStopMessage packet, Supplier<NetworkEvent.Context> ctx) {
            if (packet != null) {
                ctx.get().setPacketHandled(true);
                ctx.get().enqueueWork(() -> {
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
}
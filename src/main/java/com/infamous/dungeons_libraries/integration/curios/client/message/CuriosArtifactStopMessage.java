package com.infamous.dungeons_libraries.integration.curios.client.message;

import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsage;
import com.infamous.dungeons_libraries.capabilities.artifact.ArtifactUsageHelper;
import com.infamous.dungeons_libraries.items.artifacts.ArtifactItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;
import java.util.function.Supplier;

public class CuriosArtifactStopMessage {
    private final int slot;

    public CuriosArtifactStopMessage(int slot) {
        this.slot = slot;
    }

    public static void encode(CuriosArtifactStopMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.slot);
    }

    public static CuriosArtifactStopMessage decode(FriendlyByteBuf buf) {
        return new CuriosArtifactStopMessage(buf.readInt());
    }

    public static class CuriosArtifactHandler {
        public static void handle(CuriosArtifactStopMessage packet, Supplier<NetworkEvent.Context> ctx) {
            if (packet != null) {
                ctx.get().setPacketHandled(true);
                ctx.get().enqueueWork(() -> {
                    AbstractClientPlayer player = Minecraft.getInstance().player;
                    if (player != null) {
                        ArtifactUsage cap = ArtifactUsageHelper.getArtifactUsageCapability(player);
                        if (cap != null) {
                            ItemStack artifactStack = cap.getUsingArtifact();
                            if (artifactStack != null && artifactStack.getItem() instanceof ArtifactItem artifactItem) {
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
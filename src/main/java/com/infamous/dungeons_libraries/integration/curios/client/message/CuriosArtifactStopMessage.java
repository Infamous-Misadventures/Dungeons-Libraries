package com.infamous.dungeons_libraries.integration.curios.client.message;

import com.infamous.dungeons_libraries.network.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CuriosArtifactStopMessage {

    public CuriosArtifactStopMessage() {
    }

    public void encode(FriendlyByteBuf buf) {

    }

    public static CuriosArtifactStopMessage decode(FriendlyByteBuf buf) {
        return new CuriosArtifactStopMessage();
    }

    public static void handle(CuriosArtifactStopMessage packet, Supplier<NetworkEvent.Context> ctx) {
        ClientHandler.handleCuriosArtifactStopMessage(packet, ctx);
        ctx.get().setPacketHandled(true);
    }

}
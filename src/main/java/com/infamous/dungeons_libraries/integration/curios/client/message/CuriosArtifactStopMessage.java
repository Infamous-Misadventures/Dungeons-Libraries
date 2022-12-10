package com.infamous.dungeons_libraries.integration.curios.client.message;

import com.infamous.dungeons_libraries.network.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CuriosArtifactStopMessage {

    public CuriosArtifactStopMessage() {
    }

    public void encode(PacketBuffer buf) {

    }

    public static CuriosArtifactStopMessage decode(PacketBuffer buf) {
        return new CuriosArtifactStopMessage();
    }

    public static void onPacketReceived(CuriosArtifactStopMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientHandler.handleCuriosArtifactStopMessage(message, contextSupplier);
        contextSupplier.get().setPacketHandled(true);
    }

}
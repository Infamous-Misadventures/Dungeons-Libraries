package com.infamous.dungeons_libraries.network;

import com.infamous.dungeons_libraries.combat.DualWieldHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchHandMessage {

    public SwitchHandMessage() {
    }

    public static void encode(SwitchHandMessage packet, FriendlyByteBuf buf) {

    }

    public static SwitchHandMessage decode(FriendlyByteBuf buf) {
        return new SwitchHandMessage();
    }

    public static class SwitchHandHandler {
        public static void handle(SwitchHandMessage packet, Supplier<NetworkEvent.Context> ctx) {
            if (packet != null) {
                ctx.get().setPacketHandled(true);
                ctx.get().enqueueWork(() -> {
                    ServerPlayer player = ctx.get().getSender();
                    if (player != null) {
                        DualWieldHandler.switchHand(player);
                    }
                });
            }
        }
    }
}

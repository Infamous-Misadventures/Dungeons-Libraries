package com.infamous.dungeons_libraries.network;

import com.infamous.dungeons_libraries.combat.DualWieldHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SwitchHandMessage {

    public SwitchHandMessage() {
    }

    public static void encode(SwitchHandMessage packet, PacketBuffer buf) {

    }

    public static SwitchHandMessage decode(PacketBuffer buf) {
        return new SwitchHandMessage();
    }

    public static class SwitchHandHandler {
        public static void handle(SwitchHandMessage packet, Supplier<NetworkEvent.Context> ctx) {
            if (packet != null) {
                ctx.get().setPacketHandled(true);
                ctx.get().enqueueWork(() -> {
                    ServerPlayerEntity player = ctx.get().getSender();
                    if (player != null) {
                        DualWieldHandler.switchHand(player);
                    }
                });
            }
        }
    }
}

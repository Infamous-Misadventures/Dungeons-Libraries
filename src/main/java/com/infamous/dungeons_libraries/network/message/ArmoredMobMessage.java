package com.infamous.dungeons_libraries.network.message;

import com.infamous.dungeons_libraries.capabilities.armored.ArmoredMob;
import com.infamous.dungeons_libraries.capabilities.armored.ArmoredMobHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ArmoredMobMessage {
    private int entityId;
    private boolean isArmored;

    public ArmoredMobMessage(int entityId, boolean isArmored) {
        this.entityId = entityId;
        this.isArmored = isArmored;
    }

    public static ArmoredMobMessage decode(PacketBuffer buffer) {
        int entityId = buffer.readInt();
        boolean ancient = buffer.readBoolean();

        return new ArmoredMobMessage(entityId, ancient);
    }

    public static boolean onPacketReceived(ArmoredMobMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
                if (entity instanceof LivingEntity) {
                    ArmoredMob cap = ArmoredMobHelper.getArmoredMobCapability(entity);
                    if(cap == null) return;
                    cap.setArmored(message.isArmored);
                    if(cap.isArmored()) {
                        entity.refreshDimensions();
                    }
                }
            });
        }
        return true;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(isArmored);
    }
}

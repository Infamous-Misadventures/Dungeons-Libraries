package com.infamous.dungeons_libraries.network.message;

import com.infamous.dungeons_libraries.capabilities.elite.EliteMob;
import com.infamous.dungeons_libraries.capabilities.elite.EliteMobHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class EliteMobMessage {
    private int entityId;
    private boolean isElite;
    private ResourceLocation texture;

    public EliteMobMessage(int entityId, boolean isElite, ResourceLocation texture) {
        this.entityId = entityId;
        this.isElite = isElite;
        this.texture = texture;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(isElite);
        buffer.writeResourceLocation(texture);
    }

    public static EliteMobMessage decode(PacketBuffer buffer) {
        int entityId = buffer.readInt();
        boolean isElite = buffer.readBoolean();
        ResourceLocation texture = buffer.readResourceLocation();

        return new EliteMobMessage(entityId, isElite, texture);
    }

    public static boolean onPacketReceived(EliteMobMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
                if (entity instanceof LivingEntity) {
                    EliteMob cap = EliteMobHelper.getEliteMobCapability(entity);
                    if(cap == null) return;
                    cap.setElite(message.isElite);
                    cap.setTexture(message.texture);
                    if(cap.isElite()) {
                        entity.refreshDimensions();
                    }
                }
            });
        }
        return true;
    }
}

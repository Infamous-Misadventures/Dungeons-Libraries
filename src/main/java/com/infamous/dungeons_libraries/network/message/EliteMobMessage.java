package com.infamous.dungeons_libraries.network.message;

import com.infamous.dungeons_libraries.network.client.ClientHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
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

    public static void onPacketReceived(EliteMobMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientHandler.handleEliteMobMessage(message, contextSupplier);
        contextSupplier.get().setPacketHandled(true);
    }

    public int getEntityId() {
        return entityId;
    }

    public boolean isElite() {
        return isElite;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

}

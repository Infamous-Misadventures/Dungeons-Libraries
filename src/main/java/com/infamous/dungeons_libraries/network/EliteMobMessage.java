package com.infamous.dungeons_libraries.network;

import com.infamous.dungeons_libraries.network.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EliteMobMessage {
    private final int entityId;
    private final boolean isElite;
    private final ResourceLocation texture;

    public EliteMobMessage(int entityId, boolean isElite, ResourceLocation texture) {
        this.entityId = entityId;
        this.isElite = isElite;
        this.texture = texture;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(isElite);
        buffer.writeResourceLocation(texture);
    }

    public static EliteMobMessage decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        boolean isElite = buffer.readBoolean();
        ResourceLocation texture = buffer.readResourceLocation();

        return new EliteMobMessage(entityId, isElite, texture);
    }

    public static void handle(EliteMobMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

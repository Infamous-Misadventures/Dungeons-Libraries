package com.infamous.dungeons_libraries.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BreakItemMessage {
    private final ItemStack stack;
    private final int entityID;

    public BreakItemMessage(int entityID, ItemStack stack) {
        this.stack = stack;
        this.entityID = entityID;
    }

    public static void encode(BreakItemMessage packet, PacketBuffer buf) {
        buf.writeInt(packet.entityID);
        buf.writeItem(packet.stack);
    }

    public static BreakItemMessage decode(PacketBuffer buf) {
        return new BreakItemMessage(buf.readInt(), buf.readItem());
    }

    public static class BreakItemHandler {
        public static void handle(BreakItemMessage packet, Supplier<NetworkEvent.Context> ctx) {
            if (packet != null) {
                ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new SafeRunnable() {

                    private static final long serialVersionUID = 1;

                    @Override
                    public void run() {
                        ClientWorld world = Minecraft.getInstance().level;
                        Entity target = null;
                        if (world != null)
                            target = world.getEntity(packet.entityID);
                        if (target instanceof LivingEntity) {
                            ((LivingEntity) target).breakItem(packet.stack);
                        }
                    }

                }));
            }
        }
    }
}

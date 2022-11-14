package com.infamous.dungeons_libraries.network;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraftforge.registries.ForgeRegistries.ENCHANTMENTS;

public class BuiltInEnchantmentsMessage {
    private int entityId;
    private ResourceLocation resourceLocation;
    private List<EnchantmentData> enchantmentDataList;

    public BuiltInEnchantmentsMessage(int entityId, ResourceLocation resourceLocation, List<EnchantmentData> enchantmentDataList) {
        this.entityId = entityId;
        this.resourceLocation = resourceLocation;
        this.enchantmentDataList = enchantmentDataList;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeResourceLocation(resourceLocation);
        buffer.writeVarInt(enchantmentDataList.size());
        this.enchantmentDataList.forEach(enchantmentData -> {
            buffer.writeResourceLocation(enchantmentData.enchantment.getRegistryName());
            buffer.writeInt(enchantmentData.level);
        });
    }

    public static BuiltInEnchantmentsMessage decode(PacketBuffer buffer) {
        int entityId = buffer.readInt();
        ResourceLocation resourceLocation = buffer.readResourceLocation();
        List<EnchantmentData> enchantmentData = new ArrayList<>();
        int length = buffer.readVarInt();
        for (int x = 0; x < length; x++) {
            enchantmentData.add(new EnchantmentData(ENCHANTMENTS.getValue(buffer.readResourceLocation()), buffer.readInt()));
        }

        return new BuiltInEnchantmentsMessage(entityId, resourceLocation, enchantmentData);
    }

    public static boolean onPacketReceived(BuiltInEnchantmentsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
/*        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
                if (entity instanceof LivingEntity) {
                    getEnchantableCapabilityLazy(entity).ifPresent(iEnchantable -> {
                        iEnchantable.clearAllEnchantments();
                        message.enchantmentDataList.forEach(iEnchantable::addEnchantment);
                    });
                }
            });
        }*/
        return true;
    }
}

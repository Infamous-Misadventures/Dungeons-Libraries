package com.infamous.dungeons_libraries.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.minecraftforge.registries.ForgeRegistries.ENCHANTMENTS;

public class BuiltInEnchantmentsMessage {
    private int entityId;
    private ResourceLocation resourceLocation;
    private List<EnchantmentInstance> enchantmentInstanceList;

    public BuiltInEnchantmentsMessage(int entityId, ResourceLocation resourceLocation, List<EnchantmentInstance> enchantmentInstanceList) {
        this.entityId = entityId;
        this.resourceLocation = resourceLocation;
        this.enchantmentInstanceList = enchantmentInstanceList;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(this.entityId);
        buffer.writeResourceLocation(resourceLocation);
        buffer.writeVarInt(enchantmentInstanceList.size());
        this.enchantmentInstanceList.forEach(enchantmentInstance -> {
            buffer.writeResourceLocation(ENCHANTMENTS.getKey(enchantmentInstance.enchantment));
            buffer.writeInt(enchantmentInstance.level);
        });
    }

    public static BuiltInEnchantmentsMessage decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        ResourceLocation resourceLocation = buffer.readResourceLocation();
        List<EnchantmentInstance> enchantmentInstance = new ArrayList<>();
        int length = buffer.readVarInt();
        for (int x = 0; x < length; x++) {
            enchantmentInstance.add(new EnchantmentInstance(ENCHANTMENTS.getValue(buffer.readResourceLocation()), buffer.readInt()));
        }

        return new BuiltInEnchantmentsMessage(entityId, resourceLocation, enchantmentInstance);
    }

    public static boolean onPacketReceived(BuiltInEnchantmentsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
/*        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level.getEntity(message.entityId);
                if (entity instanceof LivingEntity) {
                    getEnchantableCapabilityLazy(entity).ifPresent(iEnchantable -> {
                        iEnchantable.clearAllEnchantments();
                        message.enchantmentInstanceList.forEach(iEnchantable::addEnchantment);
                    });
                }
            });
        }*/
        return true;
    }
}

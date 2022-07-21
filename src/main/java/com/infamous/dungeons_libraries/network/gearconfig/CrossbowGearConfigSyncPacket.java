package com.infamous.dungeons_libraries.network.gearconfig;

import com.infamous.dungeons_libraries.items.gearconfig.BowGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGearConfigRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.infamous.dungeons_libraries.items.GearConfigReloadListener.reloadAllItems;

public class CrossbowGearConfigSyncPacket {
    private static final Codec<Map<ResourceLocation, BowGearConfig>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, BowGearConfig.CODEC);

    public final Map<ResourceLocation, BowGearConfig> data;

    public CrossbowGearConfigSyncPacket(Map<ResourceLocation, BowGearConfig> data) {
        this.data = data;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeNbt((CompoundNBT) (MAPPER.encodeStart(NBTDynamicOps.INSTANCE, this.data).result().orElse(new CompoundNBT())));
    }

    public static CrossbowGearConfigSyncPacket decode(PacketBuffer buffer) {
        return new CrossbowGearConfigSyncPacket(MAPPER.parse(NBTDynamicOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        CrossbowGearConfigRegistry.CROSSBOW_GEAR_CONFIGS.data = this.data;
        reloadAllItems();
    }
}

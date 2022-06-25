package com.infamous.dungeons_libraries.network.gearconfig;

import com.infamous.dungeons_libraries.items.gearconfig.ArmorGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGearConfigRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.infamous.dungeons_libraries.items.gearconfig.GearConfigReload.reloadAll;

public class ArmorGearConfigSyncPacket {
    private static final Codec<Map<ResourceLocation, ArmorGearConfig>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, ArmorGearConfig.CODEC);

    public final Map<ResourceLocation, ArmorGearConfig> data;

    public ArmorGearConfigSyncPacket(Map<ResourceLocation, ArmorGearConfig> data) {
        this.data = data;
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeNbt((CompoundNBT) (MAPPER.encodeStart(NBTDynamicOps.INSTANCE, this.data).result().orElse(new CompoundNBT())));
    }

    public static ArmorGearConfigSyncPacket decode(PacketBuffer buffer) {
        return new ArmorGearConfigSyncPacket(MAPPER.parse(NBTDynamicOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        ArmorGearConfigRegistry.ARMOR_GEAR_CONFIGS.data = this.data;
        reloadAll();
    }
}

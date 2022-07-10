package com.infamous.dungeons_libraries.network.gearconfig;

import com.infamous.dungeons_libraries.items.gearconfig.ArmorGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.ArmorGearConfigRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

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

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt((CompoundTag) (MAPPER.encodeStart(NbtOps.INSTANCE, this.data).result().orElse(new CompoundTag())));
    }

    public static ArmorGearConfigSyncPacket decode(FriendlyByteBuf buffer) {
        return new ArmorGearConfigSyncPacket(MAPPER.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
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

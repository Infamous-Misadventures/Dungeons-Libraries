package com.infamous.dungeons_libraries.network.gearconfig;

import com.infamous.dungeons_libraries.items.gearconfig.BowGearConfig;
import com.infamous.dungeons_libraries.items.gearconfig.CrossbowGearConfigRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

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

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt((CompoundTag) (MAPPER.encodeStart(NbtOps.INSTANCE, this.data).result().orElse(new CompoundTag())));
    }

    public static CrossbowGearConfigSyncPacket decode(FriendlyByteBuf buffer) {
        return new CrossbowGearConfigSyncPacket(MAPPER.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        CrossbowGearConfigRegistry.CROSSBOW_GEAR_CONFIGS.setData(this.data);
        reloadAllItems();
    }
}

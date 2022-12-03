package com.infamous.dungeons_libraries.network.materials;

import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterials;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.items.GearConfigReloadListener.reloadAllItems;

public class ArmorMaterialSyncPacket {
    private static final Codec<Map<ResourceLocation, ArmorMaterial>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, DungeonsArmorMaterial.CODEC);

    public final Map<ResourceLocation, ArmorMaterial> data;

    public ArmorMaterialSyncPacket(Map<ResourceLocation, ArmorMaterial> data) {
        this.data = data.entrySet().stream().filter(entry -> entry.getValue() instanceof DungeonsArmorMaterial).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeNbt((CompoundTag) (MAPPER.encodeStart(NbtOps.INSTANCE, this.data).result().orElse(new CompoundTag())));
    }

    public static ArmorMaterialSyncPacket decode(FriendlyByteBuf buffer) {
        return new ArmorMaterialSyncPacket(MAPPER.parse(NbtOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        DungeonsArmorMaterials.ARMOR_MATERIALS.setData(this.data);
        reloadAllItems();
    }
}

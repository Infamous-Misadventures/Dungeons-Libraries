package com.infamous.dungeons_libraries.network.materials;

import com.infamous.dungeons_libraries.items.materials.armor.ArmorMaterials;
import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.mojang.serialization.Codec;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.items.gearconfig.GearConfigReload.reloadAll;

public class ArmorMaterialSyncPacket {
    private static final Codec<Map<ResourceLocation, IArmorMaterial>> MAPPER =
            Codec.unboundedMap(ResourceLocation.CODEC, DungeonsArmorMaterial.CODEC);

    public final Map<ResourceLocation, IArmorMaterial> data;

    public ArmorMaterialSyncPacket(Map<ResourceLocation, IArmorMaterial> data) {
        this.data = data.entrySet().stream().filter(entry -> entry.getValue() instanceof DungeonsArmorMaterial).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeNbt((CompoundNBT) (MAPPER.encodeStart(NBTDynamicOps.INSTANCE, this.data).result().orElse(new CompoundNBT())));
    }

    public static ArmorMaterialSyncPacket decode(PacketBuffer buffer) {
        return new ArmorMaterialSyncPacket(MAPPER.parse(NBTDynamicOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
    }

    public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
        NetworkEvent.Context context = contextGetter.get();
        context.enqueueWork(this::handlePacketOnMainThread);
        context.setPacketHandled(true);
    }

    private void handlePacketOnMainThread() {
        ArmorMaterials.ARMOR_MATERIALS.data = this.data;
        reloadAll();
    }
}

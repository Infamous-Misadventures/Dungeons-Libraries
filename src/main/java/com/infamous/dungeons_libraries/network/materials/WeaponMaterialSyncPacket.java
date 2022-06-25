package com.infamous.dungeons_libraries.network.materials;

import com.infamous.dungeons_libraries.items.materials.armor.DungeonsArmorMaterial;
import com.infamous.dungeons_libraries.items.materials.weapon.DungeonsWeaponMaterial;
import com.infamous.dungeons_libraries.items.materials.weapon.WeaponMaterials;
import com.mojang.serialization.Codec;
import net.minecraft.item.IItemTier;
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

public class WeaponMaterialSyncPacket {
    	private static final Codec<Map<ResourceLocation, IItemTier>> MAPPER =
			Codec.unboundedMap(ResourceLocation.CODEC, DungeonsWeaponMaterial.CODEC);

	public final Map<ResourceLocation, IItemTier> data;

	public WeaponMaterialSyncPacket(Map<ResourceLocation, IItemTier> data) {
		this.data = data.entrySet().stream().filter(entry -> entry.getValue() instanceof DungeonsWeaponMaterial).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeNbt((CompoundNBT) (MAPPER.encodeStart(NBTDynamicOps.INSTANCE, this.data).result().orElse(new CompoundNBT())));
	}

	public static WeaponMaterialSyncPacket decode(PacketBuffer buffer) {
		return new WeaponMaterialSyncPacket(MAPPER.parse(NBTDynamicOps.INSTANCE, buffer.readNbt()).result().orElse(new HashMap<>()));
	}

	public void onPacketReceived(Supplier<NetworkEvent.Context> contextGetter) {
		NetworkEvent.Context context = contextGetter.get();
		context.enqueueWork(this::handlePacketOnMainThread);
		context.setPacketHandled(true);
	}

	private void handlePacketOnMainThread() {
		WeaponMaterials.WEAPON_MATERIALS.data = this.data;
		reloadAll();
	}
}

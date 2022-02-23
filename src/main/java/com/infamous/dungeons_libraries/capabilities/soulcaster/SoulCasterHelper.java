package com.infamous.dungeons_libraries.capabilities.soulcaster;

import com.infamous.dungeons_libraries.items.interfaces.ISoulConsumer;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.UpdateSoulsMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class SoulCasterHelper {

    public static void addSouls(LivingEntity le, float amount) {
        ISoulCaster soulCasterCapability = getSoulCasterCapability(le);
        if (soulCasterCapability == null) return;

        float newAmount = soulCasterCapability.getSouls() + amount;
        soulCasterCapability.setSouls(newAmount, le);
        if (le instanceof ServerPlayerEntity) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) le), new UpdateSoulsMessage(soulCasterCapability.getSouls()));
        }
    }

    public static boolean consumeSouls(LivingEntity le, ItemStack itemStack) {
        if(le instanceof PlayerEntity && ((PlayerEntity) le).isCreative()) return true;
        ISoulCaster soulCasterCapability = getSoulCasterCapability(le);
        if (soulCasterCapability == null) return false;

        Item item = itemStack.getItem();
        if(item instanceof ISoulConsumer) {
            ISoulConsumer soulConsumer = (ISoulConsumer) item;
            float newAmount = soulCasterCapability.getSouls() + soulConsumer.getActivationCost(itemStack);
            soulCasterCapability.setSouls(newAmount, le);
            if (le instanceof ServerPlayerEntity) {
                NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) le), new UpdateSoulsMessage(soulCasterCapability.getSouls()));
            }
            return true;
        }
        return false;
    }

    @Nullable
    public static ISoulCaster getSoulCasterCapability(Entity entity)
    {
        LazyOptional<ISoulCaster> lazyCap = entity.getCapability(SoulCasterProvider.SOUL_CASTER_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the combo capability from the Entity!"));
        }
        return null;
    }
}

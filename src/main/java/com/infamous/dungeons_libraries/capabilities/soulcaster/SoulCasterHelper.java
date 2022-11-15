package com.infamous.dungeons_libraries.capabilities.soulcaster;

import com.infamous.dungeons_libraries.items.interfaces.ISoulConsumer;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.UpdateSoulsMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.SOUL_CASTER_CAPABILITY;

public class SoulCasterHelper {

    public static void addSouls(LivingEntity le, float amount) {
        SoulCaster soulCasterCapability = getSoulCasterCapability(le);
        float newAmount = soulCasterCapability.getSouls() + amount + 1;
        soulCasterCapability.setSouls(newAmount, le);
        if (le instanceof ServerPlayer) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) le), new UpdateSoulsMessage(soulCasterCapability.getSouls()));
        }
    }

    public static void setSouls(LivingEntity le, float amount) {
        SoulCaster soulCasterCapability = getSoulCasterCapability(le);
        float newAmount = amount;
        soulCasterCapability.setSouls(newAmount, le);
        if (le instanceof ServerPlayer) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) le), new UpdateSoulsMessage(soulCasterCapability.getSouls()));
        }
    }

    public static boolean consumeSouls(LivingEntity le, float amount) {
        if(le instanceof Player && ((Player) le).isCreative()) return true;
        SoulCaster soulCasterCapability = getSoulCasterCapability(le);

        if(soulCasterCapability.getSouls() < amount) return false;
        float newAmount = soulCasterCapability.getSouls() - amount;
        soulCasterCapability.setSouls(newAmount, le);
        if (le instanceof ServerPlayer) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) le), new UpdateSoulsMessage(soulCasterCapability.getSouls()));
        }
        return true;
    }

    public static boolean canConsumeSouls(LivingEntity le, ItemStack itemStack) {
        if(le instanceof Player && ((Player) le).isCreative()) return true;
        SoulCaster soulCasterCapability = getSoulCasterCapability(le);

        Item item = itemStack.getItem();
        if(item instanceof ISoulConsumer) {
            ISoulConsumer soulConsumer = (ISoulConsumer) item;
            return soulCasterCapability.getSouls() > soulConsumer.getActivationCost(itemStack);
        }
        return false;
    }

    public static SoulCaster getSoulCasterCapability(Entity entity)
    {
        return entity.getCapability(SOUL_CASTER_CAPABILITY).orElse(new SoulCaster());
    }
}

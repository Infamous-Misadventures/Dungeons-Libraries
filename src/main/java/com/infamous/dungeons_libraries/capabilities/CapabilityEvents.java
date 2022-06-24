package com.infamous.dungeons_libraries.capabilities;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.builtinenchants.BuiltInEnchantmentsProvider;
import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableProvider;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MasterProvider;
import com.infamous.dungeons_libraries.capabilities.minionmaster.MinionProvider;
import com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewardsProvider;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterProvider;
import com.infamous.dungeons_libraries.capabilities.timers.TimersProvider;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.UpdateSoulsMessage;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Arrays;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.isEnchantableEntity;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CapabilityEvents {
    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            //TODO 1.18: rename to master
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summonable"), new MinionProvider());
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summoner"), new MasterProvider());
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "soul_caster"), new SoulCasterProvider());
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "timers"), new TimersProvider());
        }
        if (isEnchantableEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(MODID, "enchantable"), new EnchantableProvider());
        }
        if(event.getObject() instanceof ServerPlayerEntity) {
            event.addCapability(new ResourceLocation(MODID, "playerrewards"), new PlayerRewardsProvider());
        }
    }

    @SubscribeEvent
    public static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        event.addCapability(new ResourceLocation(MODID, "built_in_enchantments"), new BuiltInEnchantmentsProvider(event.getObject()));
    }

    private static boolean canBeEnchanted(ItemStack itemStack){
        return Arrays.stream(EnchantmentType.values()).anyMatch(enchantmentType -> enchantmentType.canEnchant(itemStack.getItem()));
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getEntity()), new UpdateSoulsMessage(SoulCasterHelper.getSoulCasterCapability(event.getEntity()).getSouls()));
        }
    }
}

package com.infamous.dungeons_libraries.capabilities;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Master;
import com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewards;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.UpdateSoulsMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.MinionMasterHelper.getMasterCapability;
import static com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewardsHelper.getPlayerRewardsCapability;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CapabilityEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new UpdateSoulsMessage(SoulCasterHelper.getSoulCasterCapability(event.getEntity()).getSouls()));
        }
    }

    @SubscribeEvent
    public static void clonePlayerCaps(PlayerEvent.Clone event){
        Master oldMasterCap = getMasterCapability(event.getOriginal());
        Master newMasterCap = getMasterCapability(event.getPlayer());
        if (oldMasterCap != null && newMasterCap != null) {
            newMasterCap.copyFrom(oldMasterCap);
        }
        if(!event.isWasDeath() || DungeonsLibrariesConfig.ENABLE_KEEP_SOULS_ON_DEATH.get()) {
            SoulCaster oldSoulsCap = SoulCasterHelper.getSoulCasterCapability(event.getOriginal());
            SoulCaster newSoulsCap = SoulCasterHelper.getSoulCasterCapability(event.getPlayer());
            if (oldSoulsCap != null && newSoulsCap != null) {
                newSoulsCap.setSouls(oldSoulsCap.getSouls(), event.getPlayer());
            }
        }
        PlayerRewards oldPlayerRewardsCap = getPlayerRewardsCapability(event.getOriginal());
        PlayerRewards newPlayerRewardsCap = getPlayerRewardsCapability(event.getPlayer());
        if (oldPlayerRewardsCap != null && newPlayerRewardsCap != null) {
            newPlayerRewardsCap.setPlayerRewards(oldPlayerRewardsCap.getAllPlayerRewards());
        }
    }
}

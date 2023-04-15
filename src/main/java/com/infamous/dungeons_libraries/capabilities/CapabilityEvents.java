package com.infamous.dungeons_libraries.capabilities;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.minionmaster.Leader;
import com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewards;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCaster;
import com.infamous.dungeons_libraries.capabilities.soulcaster.SoulCasterHelper;
import com.infamous.dungeons_libraries.config.DungeonsLibrariesConfig;
import com.infamous.dungeons_libraries.network.NetworkHandler;
import com.infamous.dungeons_libraries.network.UpdateSoulsMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import static com.infamous.dungeons_libraries.capabilities.minionmaster.FollowerLeaderHelper.getLeaderCapability;
import static com.infamous.dungeons_libraries.capabilities.playerrewards.PlayerRewardsHelper.getPlayerRewardsCapability;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CapabilityEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new UpdateSoulsMessage(SoulCasterHelper.getSoulCasterCapability(event.getEntity()).getSouls()));
        }
    }

    @SubscribeEvent
    public static void clonePlayerCaps(PlayerEvent.Clone event) {
        Leader oldLeaderCap = getLeaderCapability(event.getOriginal());
        Leader newLeaderCap = getLeaderCapability(event.getEntity());
        newLeaderCap.copyFrom(oldLeaderCap);
        if (!event.isWasDeath() || DungeonsLibrariesConfig.ENABLE_KEEP_SOULS_ON_DEATH.get()) {
            SoulCaster oldSoulsCap = SoulCasterHelper.getSoulCasterCapability(event.getOriginal());
            SoulCaster newSoulsCap = SoulCasterHelper.getSoulCasterCapability(event.getEntity());
            newSoulsCap.setSouls(oldSoulsCap.getSouls(), event.getEntity());
        }
        PlayerRewards oldPlayerRewardsCap = getPlayerRewardsCapability(event.getOriginal());
        PlayerRewards newPlayerRewardsCap = getPlayerRewardsCapability(event.getEntity());
        newPlayerRewardsCap.setPlayerRewards(oldPlayerRewardsCap.getAllPlayerRewards());
    }
}

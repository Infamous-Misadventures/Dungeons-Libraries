package com.infamous.dungeons_libraries.patreon;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.patreon.PatreonHelper.loadPatreons;

@Mod.EventBusSubscriber(modid = MODID)
public class PatreonEvents {
    private static List<Consumer<UUID>> PATREON_JOIN_CONSUMERS = new ArrayList<>();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event){
        loadPatreons(() -> PATREON_JOIN_CONSUMERS.forEach(consumer -> consumer.accept(event.getPlayer().getUUID())));
    }

    public static void onServerStart(FMLServerStartedEvent event){
        loadPatreons(() -> {});
    }

    private static void addConsumer(Consumer<UUID> consumer){
        PATREON_JOIN_CONSUMERS.add(consumer);
    }

}

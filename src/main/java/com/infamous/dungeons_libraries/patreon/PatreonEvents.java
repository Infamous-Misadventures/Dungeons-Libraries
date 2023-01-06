package com.infamous.dungeons_libraries.patreon;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.patreon.PatreonHelper.loadPatreons;

@Mod.EventBusSubscriber(modid = MODID)
public class PatreonEvents {
    private static final List<Consumer<UUID>> PATREON_JOIN_CONSUMERS = new ArrayList<>();

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        loadPatreons(() -> PATREON_JOIN_CONSUMERS.forEach(consumer -> consumer.accept(event.getEntity().getUUID())));
    }

    public static void onServerStart(ServerStartedEvent event) {
        loadPatreons(() -> {
        });
    }

    private static void addConsumer(Consumer<UUID> consumer) {
        PATREON_JOIN_CONSUMERS.add(consumer);
    }

}

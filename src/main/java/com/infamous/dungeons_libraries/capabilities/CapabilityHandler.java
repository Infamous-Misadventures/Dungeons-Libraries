package com.infamous.dungeons_libraries.capabilities;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.summoning.SummonableProvider;
import com.infamous.dungeons_libraries.capabilities.summoning.SummonerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CapabilityHandler {
    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (isSummonableEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summonable"), new SummonableProvider());
        }
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summoner"), new SummonerProvider());
        }
    }

    private static boolean isSummonableEntity(Entity entity) {
        return entity instanceof IronGolemEntity
                || entity instanceof WolfEntity
                || entity instanceof LlamaEntity
                || entity instanceof BatEntity
                || entity instanceof BeeEntity
                || entity instanceof SheepEntity;
    }
}

package com.infamous.dungeons_libraries.capabilities;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.summoning.MasterProvider;
import com.infamous.dungeons_libraries.capabilities.summoning.MinionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.capabilities.summoning.MinionMasterHelper.isMinionEntity;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class CapabilityHandler {
    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (isMinionEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summonable"), new MinionProvider());
        }
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DungeonsLibraries.MODID, "summoner"), new MasterProvider());
        }
    }
}

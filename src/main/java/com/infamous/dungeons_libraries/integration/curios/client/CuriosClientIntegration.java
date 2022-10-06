package com.infamous.dungeons_libraries.integration.curios.client;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CuriosClientIntegration {

    public static ResourceLocation curiosIconTexture = new ResourceLocation(MODID, "icon/empty_artifact_slot");

    @SubscribeEvent
    public static void onTextureStitchedPre(TextureStitchEvent.Pre event) {
        event.addSprite(curiosIconTexture);
    }
}

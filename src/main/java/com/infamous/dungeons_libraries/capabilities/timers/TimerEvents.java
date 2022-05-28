package com.infamous.dungeons_libraries.capabilities.timers;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class TimerEvents {

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event){
        ITimers timersCapability = TimersHelper.getTimersCapability(event.getEntityLiving());
        if(timersCapability != null){
            timersCapability.tickTimers();
        }
    }

    @SubscribeEvent
    public static void onPlayerUpdate(TickEvent.PlayerTickEvent event){
        ITimers timersCapability = TimersHelper.getTimersCapability(event.player);
        if(timersCapability != null && event.phase == TickEvent.Phase.START && !event.player.isSpectator() && !event.player.level.isClientSide()){
            timersCapability.tickTimers();
        }
    }
}

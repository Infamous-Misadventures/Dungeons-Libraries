package com.infamous.dungeons_libraries.capabilities.summoning;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class MinionEvents {

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event){
        IMinion cap = MinionMasterHelper.getMinionCapability(event.getEntityLiving());
        if (cap != null) {
            event.setCanceled(true);
        }
    }
}

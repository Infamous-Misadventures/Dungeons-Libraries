package com.infamous.dungeons_libraries.capabilities.minionmaster;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class MinionEvents {

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event){
        IMinion cap = MinionMasterHelper.getMinionCapability(event.getEntityLiving());
        if (cap != null && cap.isMinion()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void reAddMinionGoals(EntityJoinWorldEvent event){
        Entity entity = event.getEntity();
        if(!event.getWorld().isClientSide() && entity instanceof MobEntity) {
            MinionMasterHelper.addMinionGoals((MobEntity) entity);
        }
    }

}

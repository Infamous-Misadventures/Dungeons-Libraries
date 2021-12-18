package com.infamous.dungeons_libraries.capabilities.summoning;

import com.infamous.dungeons_libraries.DungeonsLibraries;
import com.infamous.dungeons_libraries.capabilities.summoning.goals.MasterHurtByTargetGoal;
import com.infamous.dungeons_libraries.capabilities.summoning.goals.MasterHurtTargetGoal;
import com.infamous.dungeons_libraries.capabilities.summoning.goals.MinionFollowOwnerGoal;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.capabilities.summoning.MinionMasterHelper.getMinionCapability;

@Mod.EventBusSubscriber(modid = DungeonsLibraries.MODID)
public class MinionEvents {

    @SubscribeEvent
    public static void onLivingDropsEvent(LivingDropsEvent event){
        IMinion cap = MinionMasterHelper.getMinionCapability(event.getEntityLiving());
        if (cap != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void reAddMinionGoals(EntityJoinWorldEvent event){
        IMinion minionCap = getMinionCapability(event.getEntity());
        if(minionCap == null) return;
        if(event.getEntity() instanceof IronGolemEntity && minionCap.isMinion()){
            MobEntity mobEntity = (MobEntity) event.getEntity();
            mobEntity.goalSelector.addGoal(2, new MinionFollowOwnerGoal(mobEntity, 2.1D, 10.0F, 2.0F, false));

            mobEntity.targetSelector.addGoal(1, new MasterHurtByTargetGoal(mobEntity));
            mobEntity.targetSelector.addGoal(2, new MasterHurtTargetGoal(mobEntity));
        }
    }
}

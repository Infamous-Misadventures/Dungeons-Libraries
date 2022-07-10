package com.infamous.dungeons_libraries.capabilities.soulcaster;

import com.infamous.dungeons_libraries.entities.SoulOrbEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SOUL_GATHERING;

@Mod.EventBusSubscriber(modid = MODID)
public class SoulEvents {

    @SubscribeEvent
    public static void onSoulSpawning(LivingDeathEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        Entity sourceEntity = event.getSource().getEntity();
        if(sourceEntity instanceof Player) {
            double soulAmount = ((Player) sourceEntity).getAttributeValue(SOUL_GATHERING.get());
            if(soulAmount > 0) {
                entityLiving.level.addFreshEntity(new SoulOrbEntity((Player) sourceEntity, entityLiving.level, entityLiving.getX(), entityLiving.getY() + 0.5D, entityLiving.getZ(), (float) soulAmount));
            }
        }
    }
}

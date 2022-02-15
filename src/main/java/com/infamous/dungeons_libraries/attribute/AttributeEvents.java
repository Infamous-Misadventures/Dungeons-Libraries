package com.infamous.dungeons_libraries.attribute;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SUMMON_CAP;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
public class AttributeEvents {

    @SubscribeEvent
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event){
        List<EntityType<? extends LivingEntity>> entitiesWithoutSummonCap = event.getTypes().stream().filter(entityType -> !event.has(entityType, SUMMON_CAP.get())).collect(Collectors.toList());
        entitiesWithoutSummonCap.forEach(entityType -> event.add(entityType, SUMMON_CAP.get(), 0));
    }
}

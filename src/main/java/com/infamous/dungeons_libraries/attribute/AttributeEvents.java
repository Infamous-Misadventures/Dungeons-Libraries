package com.infamous.dungeons_libraries.attribute;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.stream.Collectors;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
public class AttributeEvents {

    @SubscribeEvent
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event) {
        addAttributeToAll(event, SUMMON_CAP.get());
        addAttributeToAll(event, SOUL_GATHERING.get());
        addAttributeToAll(event, SOUL_CAP.get());
        addAttributeToAll(event, LIFE_STEAL.get());
        addAttributeToAll(event, RANGED_DAMAGE_MULTIPLIER.get());
        addAttributeToAll(event, ARTIFACT_COOLDOWN_MULTIPLIER.get());
        addAttributeToAll(event, MAGIC_DAMAGE_MULTIPLIER.get());
    }

    private static void addAttributeToAll(EntityAttributeModificationEvent event, Attribute attribute) {
        List<EntityType<? extends LivingEntity>> entitiesWithoutAttribute = event.getTypes().stream().filter(entityType -> !event.has(entityType, attribute)).collect(Collectors.toList());
        entitiesWithoutAttribute.forEach(entityType -> event.add(entityType, attribute, attribute.getDefaultValue()));
    }
}

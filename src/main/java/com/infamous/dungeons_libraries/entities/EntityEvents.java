package com.infamous.dungeons_libraries.entities;

import com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableProvider;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;
import static com.infamous.dungeons_libraries.capabilities.enchantable.EnchantableHelper.isEnchantableEntity;

@Mod.EventBusSubscriber(modid = MODID)
public class EntityEvents {
    @SubscribeEvent
    public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (isEnchantableEntity(event.getObject())) {
            event.addCapability(new ResourceLocation(MODID, "enchantable"), new EnchantableProvider());
        }
    }
}

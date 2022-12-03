package com.infamous.dungeons_libraries.entities;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public final class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final RegistryObject<EntityType<SoulOrbEntity>> SOUL_ORB = ENTITY_TYPES.register("soul_orb", () ->
            EntityType.Builder.<SoulOrbEntity>of(SoulOrbEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20)
                    .build(new ResourceLocation(MODID, "soul_orb").toString())
    );

}

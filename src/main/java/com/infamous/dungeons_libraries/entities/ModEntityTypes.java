package com.infamous.dungeons_libraries.entities;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public final class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);

    public static final RegistryObject<EntityType<SoulOrbEntity>> SOUL_ORB = ENTITY_TYPES.register("soul_orb", () ->
            EntityType.Builder.<SoulOrbEntity>of(SoulOrbEntity::new, EntityClassification.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20)
                    .build(new ResourceLocation(MODID, "soul_orb").toString())
    );

}

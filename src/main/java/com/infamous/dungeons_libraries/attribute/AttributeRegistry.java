package com.infamous.dungeons_libraries.attribute;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.infamous.dungeons_libraries.DungeonsLibraries.MODID;

public class AttributeRegistry {

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, MODID);

    public static final RegistryObject<Attribute> SUMMON_CAP = ATTRIBUTES.register("summon_cap", () -> new RangedAttribute(
            "attribute.name.generic.dungeons_libraries.summon_cap",
            0.0D,
            0.0D,
            1024.0D)
            .setSyncable(true));

    public static final RegistryObject<Attribute> SOUL_GATHERING = ATTRIBUTES.register("soul_gathering", () -> new RangedAttribute(
            "attribute.name.generic.dungeons_libraries.soul_gathering",
            0.0D,
            0.0D,
            1024.0D)
            .setSyncable(true));

    public static final RegistryObject<Attribute> SOUL_CAP = ATTRIBUTES.register("soul_cap", () -> new RangedAttribute(
            "attribute.name.generic.dungeons_libraries.soul_cap",
            300.0D,
            0.0D,
            1024.0D)
            .setSyncable(true));
}

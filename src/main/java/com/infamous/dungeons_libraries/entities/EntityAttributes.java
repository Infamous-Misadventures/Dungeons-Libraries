package com.infamous.dungeons_libraries.entities;


import com.infamous.dungeons_libraries.DungeonsLibraries;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public final class EntityAttributes {

	public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Attribute.class, DungeonsLibraries.MODID);
	public static final RegistryObject<Attribute> ATTACK_REACH = ATTRIBUTES.register("attack_reach", () -> new RangedAttribute("attribute.generic.attack_reach", 3, 0, 1024).setSyncable(true));

	private EntityAttributes() {}

}

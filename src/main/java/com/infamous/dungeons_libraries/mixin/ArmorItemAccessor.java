package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {

    @Accessor
    void setMaterial(ArmorMaterial armorMaterial);

    @Accessor
    void setDefense(int defense);

    @Accessor
    void setToughness(float toughness);

    @Accessor
    void setKnockbackResistance(float knockbackResistance);
}

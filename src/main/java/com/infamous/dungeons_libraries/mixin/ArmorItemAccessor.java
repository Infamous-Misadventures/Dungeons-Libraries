package com.infamous.dungeons_libraries.mixin;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {

    @Accessor
    @Mutable
    void setMaterial(ArmorMaterial armorMaterial);

    @Accessor
    @Mutable
    void setDefense(int defense);

    @Accessor
    @Mutable
    void setToughness(float toughness);

    @Accessor
    @Mutable
    void setKnockbackResistance(float knockbackResistance);
}

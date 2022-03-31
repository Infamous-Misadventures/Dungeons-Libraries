package com.infamous.dungeons_libraries.mixin;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorItem.class)
public interface ArmorItemAccessor {

    @Accessor
    void setMaterial(IArmorMaterial armorMaterial);

    @Accessor
    void setDefense(int defense);

    @Accessor
    void setToughness(float toughness);

    @Accessor
    void setKnockbackResistance(float knockbackResistance);
}

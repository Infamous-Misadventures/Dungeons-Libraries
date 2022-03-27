package com.infamous.dungeons_libraries.items.interfaces;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IRangedWeapon {

    boolean isUnique();

    String HARPOON_TAG = "Harpoon";
    String HUNTING_TAG = "Hunting";
    String RELIABLE_RICOCHET_TAG = "ReliableRicochet";
    String FREEZING_TAG = "Freezing";
    String EXPLOSIVE_TAG = "Explosive";
    String MULTISHOT_TAG = "Multishot";
    String DUAL_WIELD_TAG = "DualWield";
    String GALE_ARROW_TAG = "GaleArrow";

    // Non-Enchantment Abilities
    default boolean shootsFasterArrows(ItemStack stack){
        return false;
    }

    default boolean shootsExplosiveArrows(ItemStack stack){
        return false;
    }

    default boolean shootsHeavyArrows(ItemStack stack){
        return false;
    }

    default boolean setsPetsAttackTarget(ItemStack stack){
        return false;
    }

    default boolean hasExtraMultishot(ItemStack stack){
        return false;
    }

    default boolean shootsFreezingArrows(ItemStack stack){
        return false;
    }

    default boolean hasGuaranteedRicochet(ItemStack stack){ return false;}

    default boolean hasMultishotWhenCharged(ItemStack stack){ return false;}

    default boolean canDualWield(ItemStack stack){ return false;}

    default boolean hasHighFireRate(ItemStack stack){ return false;}

    default boolean hasSlowFireRate(ItemStack stack){ return this.shootsHeavyArrows(stack);}

    default boolean shootsGaleArrows(ItemStack stack){ return false;}

    default boolean hasWindUpAttack(ItemStack stack){ return false;}

    default boolean hasBubbleDamage(ItemStack itemStack){
        return false;
    }

    default boolean firesHarpoons(ItemStack itemStack){
        return false;
    }

    // Enchantment Abilities
}

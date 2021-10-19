package com.infamous.dungeons_libraries.capabilities.enchantable;

import com.infamous.dungeons_libraries.mobenchantments.MobEnchantment;
import net.minecraft.entity.Entity;

import java.util.List;

public interface IEnchantable {

    boolean addEnchantment(MobEnchantment enchantment);

    boolean removeEnchantment(MobEnchantment enchantment);

    boolean clearAllEnchantments();

    List<MobEnchantment> getEnchantments();

    boolean hasEnchantment();

    boolean hasEnchantment(MobEnchantment mobEnchantment);

    boolean isSpawned();

    void setSpawned(boolean hasSpawned);
}

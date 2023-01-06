package com.infamous.dungeons_libraries.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class ArtifactEvent extends LivingEvent {

    ItemStack itemStack;

    public ArtifactEvent(LivingEntity entity, ItemStack itemStack) {
        super(entity);
        this.itemStack = itemStack;
    }

    public static class Activated extends ArtifactEvent {
        public Activated(LivingEntity entity, ItemStack itemStack) {
            super(entity, itemStack);
        }
    }
}

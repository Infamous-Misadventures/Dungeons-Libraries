package com.infamous.dungeons_libraries.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;

public abstract class CrossbowEvent extends LivingEvent {

    ItemStack itemStack;

    public CrossbowEvent(LivingEntity entity, ItemStack itemStack) {
        super(entity);
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public static class ChargeTime extends CrossbowEvent
    {
        private float originalChargeTime;
        private float chargeTime;

        public ChargeTime(LivingEntity entity, ItemStack itemStack, float chargeTime) {
            super(entity, itemStack);
            this.originalChargeTime = chargeTime;
            this.chargeTime = chargeTime;
        }

        public float getOriginalChargeTime() {
            return originalChargeTime;
        }

        public float getChargeTime() {
            return chargeTime;
        }

        public void setChargeTime(float chargeTime) {
            this.chargeTime = chargeTime;
        }
    }

    public static class Velocity extends CrossbowEvent
    {
        private float originalVelocity;
        private float velocity;

        public Velocity(LivingEntity entity, ItemStack itemStack, float velocity) {
            super(entity, itemStack);
            this.originalVelocity = velocity;
            this.velocity = velocity;
        }

        public float getOriginalVelocity() {
            return originalVelocity;
        }

        public float getVelocity() {
            return velocity;
        }

        public void setVelocity(float velocity) {
            this.velocity = velocity;
        }
    }
}

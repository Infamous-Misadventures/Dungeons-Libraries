package com.infamous.dungeons_libraries.capabilities.soulcaster;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SOUL_CAP;

public class SoulCaster implements ISoulCaster {

    private float souls;

    public SoulCaster() {
        this.souls = 0;
    }

    @Override
    public float getSouls() {
        return souls;
    }

    @Override
    public void addSouls(float amount, LivingEntity living) {
        setSouls(this.getSouls() + amount, living);
    }

    @Override
    public void setSouls(float amount, @Nullable LivingEntity living) {
        if (living != null) {
            this.souls = MathHelper.clamp(amount, 0, (float) living.getAttributeValue(SOUL_CAP.get()));
        } else {
            this.souls = Math.max(amount, 0);
        }
    }
}

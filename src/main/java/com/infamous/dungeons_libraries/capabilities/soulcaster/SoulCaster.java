package com.infamous.dungeons_libraries.capabilities.soulcaster;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

import static com.infamous.dungeons_libraries.attribute.AttributeRegistry.SOUL_CAP;

public class SoulCaster implements INBTSerializable<CompoundTag> {

    private float souls;

    public SoulCaster() {
        this.souls = 0;
    }

    public float getSouls() {
        return souls;
    }

    public void addSouls(float amount, LivingEntity living) {
        setSouls(this.getSouls() + amount, living);
    }

    public void setSouls(float amount, @Nullable LivingEntity living) {
        if (living != null) {
            this.souls = Mth.clamp(amount, 0, (float) living.getAttributeValue(SOUL_CAP.get()));
        } else {
            this.souls = Math.max(amount, 0);
        }
    }

    @Nullable
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("souls", this.getSouls());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.setSouls(tag.getFloat("souls"), null);
    }
}

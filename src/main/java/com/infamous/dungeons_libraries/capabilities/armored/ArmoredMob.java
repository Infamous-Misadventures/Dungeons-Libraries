package com.infamous.dungeons_libraries.capabilities.armored;

import net.minecraft.nbt.CompoundNBT;

public class ArmoredMob {
    private boolean isArmored = false;
    private boolean hasSpawned = false;

    public boolean isArmored() {
        return isArmored;
    }

    public void setArmored(boolean armored) {
        isArmored = armored;
    }

    public boolean hasSpawned() {
        return hasSpawned;
    }

    public void setHasSpawned(boolean hasSpawned) {
        this.hasSpawned = hasSpawned;
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putBoolean("isArmored", isArmored);
        nbt.putBoolean("hasSpawned", hasSpawned);
        return nbt;
    }

    public void deserializeNBT(CompoundNBT tag) {
        isArmored = tag.getBoolean("isArmored");
        hasSpawned = tag.getBoolean("hasSpawned");
    }

}

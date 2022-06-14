package com.infamous.dungeons_libraries.items.interfaces;

public interface IArmor {

    boolean isUnique();

    default boolean doGivesYouAPetBat() {
        return false;
    }

    default double getFreezingResistance() {
        return 0;
    }
}

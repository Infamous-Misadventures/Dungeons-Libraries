package com.infamous.dungeons_libraries.patreon;

public enum PatreonLevel {
    CAULDRON("cauldron", 1),
    MONSTROSITY("monstrosity", 2),
    ARCHILLAGER("archillager", 3),
    STAFF("staff", 99),
    UNKNOWN("unknown", 0);
    
    private final String name;
    private final int level;

    PatreonLevel(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public static PatreonLevel byName(String name, PatreonLevel defaultRank) {
        for (PatreonLevel factionRank : values()) {
            if (factionRank.name.equals(name)) {
                return factionRank;
            }
        }
        return defaultRank;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}

package com.infamous.dungeons_libraries.patreon;

import java.util.UUID;

public class Patreon {
    public static final Patreon DEFAULT = new Patreon(null, PatreonLevel.UNKNOWN, "default");

    private UUID uuid;
    private PatreonLevel level;
    private String username;

    public Patreon(UUID uuid, PatreonLevel level, String username) {
        this.uuid = uuid;
        this.level = level;
        this.username = username;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PatreonLevel getLevel() {
        return level;
    }

    public String getUsername() {
        return username;
    }
}

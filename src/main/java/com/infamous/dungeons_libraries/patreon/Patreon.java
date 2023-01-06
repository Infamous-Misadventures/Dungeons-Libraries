package com.infamous.dungeons_libraries.patreon;

import java.util.UUID;

public class Patreon {
    public static final Patreon DEFAULT = new Patreon(null, PatreonLevel.UNKNOWN, "default");

    private final UUID uuid;
    private final PatreonLevel level;
    private final String username;

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

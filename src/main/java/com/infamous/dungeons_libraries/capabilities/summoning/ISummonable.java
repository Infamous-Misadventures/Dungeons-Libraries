package com.infamous.dungeons_libraries.capabilities.summoning;

import java.util.UUID;

public interface ISummonable {

    UUID getSummoner();

    void setSummoner(UUID summoner);

}

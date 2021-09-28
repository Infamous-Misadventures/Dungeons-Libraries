package com.infamous.dungeons_libraries.utils;

import com.infamous.dungeons_libraries.capabilities.summoning.ISummonable;
import com.infamous.dungeons_libraries.capabilities.summoning.ISummoner;
import com.infamous.dungeons_libraries.capabilities.summoning.SummonableProvider;
import com.infamous.dungeons_libraries.capabilities.summoning.SummonerProvider;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class CapabilityHelper {

    @Nullable
    public static ISummoner getSummonerCapability(Entity entity)
    {
        LazyOptional<ISummoner> lazyCap = entity.getCapability(SummonerProvider.SUMMONER_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summoner capability from the Entity!"));
        }
        return null;
    }

    @Nullable
    public static ISummonable getSummonableCapability(Entity entity)
    {
        LazyOptional<ISummonable> lazyCap = entity.getCapability(SummonableProvider.SUMMONABLE_CAPABILITY);
        if (lazyCap.isPresent()) {
            return lazyCap.orElseThrow(() -> new IllegalStateException("Couldn't get the summonable capability from the Entity!"));
        }
        return null;
    }

}

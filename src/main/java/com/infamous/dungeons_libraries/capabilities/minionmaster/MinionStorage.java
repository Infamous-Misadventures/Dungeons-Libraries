package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class MinionStorage implements Capability.IStorage<IMinion> {

    public static final String MASTER_KEY = "summoner";
    public static final String LEVEL_KEY = "level";
    public static final String SUMMON_FLAG_KEY = "is_summon";
    public static final String TEMPORARY_FLAG_KEY = "is_temporary";
    public static final String REVERTS_ON_EXPIRATION_FLAG_KEY = "reverts_on_expiration";
    public static final String MINION_TIMER_KEY = "minion_timer";

    @Nullable
    @Override
    public INBT writeNBT(Capability<IMinion> capability, IMinion instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        if(instance.getMaster() != null){
            tag.putUUID(MASTER_KEY, instance.getMaster().getUUID());
            ResourceLocation location = instance.getMaster().level.dimension().location();
            tag.putString(LEVEL_KEY, location.toString());
        }
        tag.putBoolean(SUMMON_FLAG_KEY, instance.isSummon());
        tag.putBoolean(TEMPORARY_FLAG_KEY, instance.isTemporary());
        tag.putBoolean(REVERTS_ON_EXPIRATION_FLAG_KEY, instance.revertsOnExpiration());
        tag.putInt(MINION_TIMER_KEY, instance.getMinionTimer());
        return tag;
    }

    @Override
    public void readNBT(Capability<IMinion> capability, IMinion instance, Direction side, INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        if(tag.hasUUID(MASTER_KEY)){
            instance.setMasterUUID(tag.getUUID(MASTER_KEY));
        }
        if(tag.contains(LEVEL_KEY)) {
            instance.setLevelOnLoad(new ResourceLocation(tag.getString(LEVEL_KEY)));
        }
        if(tag.contains(SUMMON_FLAG_KEY)){
            instance.setSummon(tag.getBoolean(SUMMON_FLAG_KEY));
        }
        if(tag.contains(TEMPORARY_FLAG_KEY)){
            instance.setTemporary(tag.getBoolean(TEMPORARY_FLAG_KEY));
        }
        if(tag.contains(REVERTS_ON_EXPIRATION_FLAG_KEY)){
            instance.setRevertsOnExpiration(tag.getBoolean(REVERTS_ON_EXPIRATION_FLAG_KEY));
        }
        if(tag.contains(MINION_TIMER_KEY)){
            instance.setMinionTimer(tag.getInt(MINION_TIMER_KEY));
        }
    }
}

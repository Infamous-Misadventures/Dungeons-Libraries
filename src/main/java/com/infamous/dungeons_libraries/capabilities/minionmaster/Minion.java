package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.UUID;

import static com.infamous.dungeons_libraries.capabilities.ModCapabilities.MINION_CAPABILITY;

public class Minion implements INBTSerializable<CompoundTag> {

    private UUID masterUUID;
    private ResourceLocation levelOnLoad;
    private LivingEntity master;
    private boolean summon = false;
    private int minionTimer = 0;
    private boolean temporary = false;
    private boolean revertsOnExpiration = false;

    public LivingEntity getMaster() {
        if(this.master == null && this.masterUUID != null && this.levelOnLoad != null){
            ResourceKey<Level> registrykey1 = ResourceKey.create(Registry.DIMENSION_REGISTRY, this.levelOnLoad);
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerLevel world = server.getLevel(registrykey1);
            if (world != null) {
                Entity entity = world.getEntity(masterUUID);
                if (entity instanceof LivingEntity) {
                    this.master = (LivingEntity) entity;
                }
            }
        }
        return this.master;
    }

    public void setMaster(LivingEntity master) {
        this.master = master;
        this.masterUUID = master.getUUID();
    }

    public void setMasterUUID(UUID masterUUID) {
        this.masterUUID = masterUUID;
    }

    public void setLevelOnLoad(ResourceLocation levelOnLoad) {
        this.levelOnLoad = levelOnLoad;
    }

    public boolean isMinion() {
        return getMaster() != null;
    }

    public boolean isSummon() {
        return getMaster() != null && this.summon == true;
    }

    public void setSummon(boolean summon) {
        this.summon = summon;
    }

    public void setMinionTimer(int minionTimer) {
        this.minionTimer = minionTimer;
    }

    public int getMinionTimer() {
        return this.minionTimer;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public boolean revertsOnExpiration() {
        return revertsOnExpiration;
    }

    public void setRevertsOnExpiration(boolean revertsOnExpiration) {
        this.revertsOnExpiration = revertsOnExpiration;
    }

    public static final String MASTER_KEY = "summoner";
    public static final String LEVEL_KEY = "level";
    public static final String SUMMON_FLAG_KEY = "is_summon";
    public static final String TEMPORARY_FLAG_KEY = "is_temporary";
    public static final String REVERTS_ON_EXPIRATION_FLAG_KEY = "reverts_on_expiration";
    public static final String MINION_TIMER_KEY = "minion_timer";

    @Nullable
    @Override
    public CompoundTag serializeNBT() {
        if (MINION_CAPABILITY == null) {
            return new CompoundTag();
        }
        CompoundTag tag = new CompoundTag();
        if(this.getMaster() != null){
            tag.putUUID(MASTER_KEY, this.getMaster().getUUID());
            ResourceLocation location = this.getMaster().level.dimension().location();
            tag.putString(LEVEL_KEY, location.toString());
        }
        tag.putBoolean(SUMMON_FLAG_KEY, this.isSummon());
        tag.putBoolean(TEMPORARY_FLAG_KEY, this.isTemporary());
        tag.putBoolean(REVERTS_ON_EXPIRATION_FLAG_KEY, this.revertsOnExpiration());
        tag.putInt(MINION_TIMER_KEY, this.getMinionTimer());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if(tag.hasUUID(MASTER_KEY)){
            this.setMasterUUID(tag.getUUID(MASTER_KEY));
        }
        if(tag.contains(LEVEL_KEY)) {
            this.setLevelOnLoad(new ResourceLocation(tag.getString(LEVEL_KEY)));
        }
        if(tag.contains(SUMMON_FLAG_KEY)){
            this.setSummon(tag.getBoolean(SUMMON_FLAG_KEY));
        }
        if(tag.contains(TEMPORARY_FLAG_KEY)){
            this.setTemporary(tag.getBoolean(TEMPORARY_FLAG_KEY));
        }
        if(tag.contains(REVERTS_ON_EXPIRATION_FLAG_KEY)){
            this.setRevertsOnExpiration(tag.getBoolean(REVERTS_ON_EXPIRATION_FLAG_KEY));
        }
        if(tag.contains(MINION_TIMER_KEY)){
            this.setMinionTimer(tag.getInt(MINION_TIMER_KEY));
        }
    }
}

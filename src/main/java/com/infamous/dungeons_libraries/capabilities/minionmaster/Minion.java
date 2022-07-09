package com.infamous.dungeons_libraries.capabilities.minionmaster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.UUID;

public class Minion implements IMinion {

    private UUID masterUUID;
    private ResourceLocation levelOnLoad;
    private LivingEntity master;
    private boolean summon = false;
    private int minionTimer = 0;
    private boolean temporary = false;
    private boolean revertsOnExpiration = false;

    @Override
    public LivingEntity getMaster() {
        if(this.master == null && this.masterUUID != null && this.levelOnLoad != null){
            RegistryKey<World> registrykey1 = RegistryKey.create(Registry.DIMENSION_REGISTRY, this.levelOnLoad);
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ServerWorld world = server.getLevel(registrykey1);
            if (world != null) {
                Entity entity = world.getEntity(masterUUID);
                if (entity instanceof LivingEntity) {
                    this.master = (LivingEntity) entity;
                }
            }
        }
        return this.master;
    }

    @Override
    public void setMaster(LivingEntity master) {
        this.master = master;
        this.masterUUID = master.getUUID();
    }

    @Override
    public void setMasterUUID(UUID masterUUID) {
        this.masterUUID = masterUUID;
    }

    @Override
    public void setLevelOnLoad(ResourceLocation levelOnLoad) {
        this.levelOnLoad = levelOnLoad;
    }

    @Override
    public boolean isMinion() {
        return getMaster() != null;
    }

    @Override
    public boolean isSummon() {
        return getMaster() != null && this.summon == true;
    }

    @Override
    public void setSummon(boolean summon) {
        this.summon = summon;
    }

    @Override
    public void setMinionTimer(int minionTimer) {
        this.minionTimer = minionTimer;
    }

    @Override
    public int getMinionTimer() {
        return this.minionTimer;
    }

    @Override
    public boolean isTemporary() {
        return temporary;
    }

    @Override
    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    @Override
    public boolean revertsOnExpiration() {
        return revertsOnExpiration;
    }

    @Override
    public void setRevertsOnExpiration(boolean revertsOnExpiration) {
        this.revertsOnExpiration = revertsOnExpiration;
    }
}

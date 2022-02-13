package com.infamous.dungeons_libraries.capabilities.minionmaster;

import javax.annotation.Nullable;
import java.util.UUID;

public class Minion implements IMinion {

    @Nullable
    private UUID master;

    @Override
    public UUID getMaster() {
        return this.master;
    }

    @Override
    public void setMaster(@Nullable UUID master) {
        this.master = master;
    }

    @Override
    public boolean isMinion() {
        return master == null;
    }


}

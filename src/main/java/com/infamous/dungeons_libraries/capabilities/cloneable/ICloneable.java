package com.infamous.dungeons_libraries.capabilities.cloneable;

import java.util.UUID;

public interface ICloneable {

    boolean addClone(UUID clone);

    UUID[] getClones();
}

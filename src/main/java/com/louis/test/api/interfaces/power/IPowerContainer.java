package com.louis.test.api.interfaces.power;

import com.enderio.core.common.util.BlockCoord;

public interface IPowerContainer {

    int getEnergyStored();

    void setEnergyStored(int storedEnergy);

    BlockCoord getLocation();
}

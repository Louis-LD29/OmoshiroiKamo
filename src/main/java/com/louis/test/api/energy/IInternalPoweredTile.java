package com.louis.test.api.energy;

import cofh.api.energy.IEnergyConnection;
import com.louis.test.api.material.VoltageTier;
import net.minecraftforge.common.util.ForgeDirection;

public interface IInternalPoweredTile extends IPowerContainer, IEnergyConnection {

    int getMaxEnergyRecieved(ForgeDirection dir);

    int getMaxEnergyStored();

    VoltageTier getVoltageTier();

    /**
     * Should the power be displayed in WAILA or other places
     */
    boolean displayPower();
}

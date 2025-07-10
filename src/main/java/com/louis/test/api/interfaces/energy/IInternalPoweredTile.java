package com.louis.test.api.interfaces.energy;

import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.api.enums.VoltageTier;

import cofh.api.energy.IEnergyConnection;

public interface IInternalPoweredTile extends IPowerContainer, IEnergyConnection {

    int getMaxEnergyRecieved(ForgeDirection dir);

    int getMaxEnergyStored();

    VoltageTier getVoltageTier();

    /**
     * Should the power be displayed in WAILA or other places
     */
    boolean displayPower();
}

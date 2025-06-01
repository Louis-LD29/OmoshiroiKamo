package com.louis.test.api.interfaces.power;

import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyConnection;

public interface IInternalPoweredTile extends IPowerContainer, IEnergyConnection {

    int getMaxEnergyRecieved(ForgeDirection dir);

    int getMaxEnergyStored();

    /**
     * Should the power be displayed in WAILA or other places
     */
    boolean displayPower();
}

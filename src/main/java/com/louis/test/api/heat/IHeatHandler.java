package com.louis.test.api.heat;

import net.minecraftforge.common.util.ForgeDirection;

public interface IHeatHandler {

    HeatStorage getHeat();

    float receiveHeat(ForgeDirection side, float amount, boolean doTransfer);

    float extractHeat(ForgeDirection side, float amount, boolean doTransfer);

    boolean canReceiveHeat(ForgeDirection side);

    boolean canExtractHeat(ForgeDirection side);
}

package com.louis.test.api.interfaces.fluid;

import net.minecraftforge.fluids.IFluidHandler;

public interface IFluidHandlerAdv extends IFluidHandler {

    SmartTank[] getTanks();
}

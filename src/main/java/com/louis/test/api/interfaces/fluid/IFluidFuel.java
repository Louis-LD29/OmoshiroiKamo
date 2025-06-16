package com.louis.test.api.interfaces.fluid;

import net.minecraftforge.fluids.Fluid;

public interface IFluidFuel {

    Fluid getFluid();

    int getTotalBurningTime();

    int getPowerPerCycle();
}

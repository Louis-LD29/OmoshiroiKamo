package com.louis.test.api.interfaces.fluid;

import net.minecraftforge.fluids.Fluid;

public interface IFluidCoolant {

    Fluid getFluid();

    float getDegreesCoolingPerMB(float heat);
}

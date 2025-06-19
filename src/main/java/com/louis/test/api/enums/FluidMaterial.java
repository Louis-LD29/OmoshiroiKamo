package com.louis.test.api.enums;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public enum FluidMaterial {

    // Fluid
    WATER("Water", 997, 0.00089, 298.15, false), // 25°C
    // Gas
    HYDROGEN("Hydrogen", 0.08988, 8.76e-6, 298.15, true), // 25°C
    OXYGEN("Oxygen", 1.429, 2.08e-5, 298.15, true), // 25°C
    STEAM("Steam", 0.6, 1.34e-5, 373.15, true); // 100°C

    private final String name;
    private final boolean isGas;

    private double densityKgPerM3; // kg/m³
    private double viscosityPaS; // Pa·s
    private double temperatureK; // Kelvin

    FluidMaterial(String name, double densityKgPerM3, double viscosityPaS, double temperatureK, boolean isGas) {
        this.name = name;
        this.densityKgPerM3 = densityKgPerM3;
        this.viscosityPaS = viscosityPaS;
        this.temperatureK = temperatureK;
        this.isGas = isGas;
    }

    public String getName() {
        return name;
    }

    public double getDensity() {
        return densityKgPerM3;
    }

    public void setDensity(double densityKgPerM3) {
        this.densityKgPerM3 = densityKgPerM3;
    }

    public double getViscosity() {
        return viscosityPaS;
    }

    public void setViscosity(double viscosityPaS) {
        this.viscosityPaS = viscosityPaS;
    }

    public double getTemperatureK() {
        return temperatureK;
    }

    public void setTemperatureK(double temperatureK) {
        this.temperatureK = temperatureK;
    }

    public boolean isGas() {
        return isGas;
    }

    public Fluid getFluid() {
        return FluidRegistry.getFluid(name);
    }

    public FluidStack getFluidStack(int amount) {
        Fluid fluid = getFluid();
        if (fluid != null) {
            return new FluidStack(fluid, amount);
        }
        return null;
    }

}

package louis.omoshiroikamo.api.fluid;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public enum FluidMaterial {

    // Liquid
    WATER("Water", 1000, 0.00089, 298.15, false, 0, 2.2e9), // Pa

    // Gases
    HYDROGEN("Hydrogen", 0.08988, 8.76e-6, 298.15, true, 4124, 1.0e5), // 100 kPa ~ khí lý tưởng
    OXYGEN("Oxygen", 1.429, 2.08e-5, 298.15, true, 259.8, 1.0e5),
    STEAM("Steam", 0.6, 1.34e-5, 373.15, true, 461.5, 1.0e5); // J/(kg·K)

    private final String name;
    private final boolean isGas;
    private final double gasConstantR; // J/(kg·K), chỉ cho chất khí
    private final double bulkModulusPa; // Pa

    private double densityKgPerM3; // kg/m³
    private double viscosityPaS; // Pa·s
    private double temperatureK; // Kelvin

    private static final Map<String, FluidMaterial> NAME_LOOKUP = new HashMap<>();

    FluidMaterial(String name, double densityKgPerM3, double viscosityPaS, double temperatureK, boolean isGas,
        double gasConstantR, double bulkModulusPa) {
        this.name = name;
        this.densityKgPerM3 = densityKgPerM3;
        this.viscosityPaS = viscosityPaS;
        this.temperatureK = temperatureK;
        this.isGas = isGas;
        this.gasConstantR = gasConstantR;
        this.bulkModulusPa = bulkModulusPa;
    }

    static {
        for (FluidMaterial mat : values()) {
            NAME_LOOKUP.put(mat.name.toLowerCase(Locale.ROOT), mat);
        }
    }

    public static FluidMaterial getByName(String name) {
        if (name == null) return null;
        for (FluidMaterial mat : values()) {
            if (mat.getName()
                .equalsIgnoreCase(name)) return mat;
        }
        return null;
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

    public double getGasConstant() {
        return gasConstantR;
    }

    public double getBulkModulus() {
        return bulkModulusPa;
    }

    public Fluid getFluid() {
        return FluidRegistry.getFluid(getName());
    }

    public FluidStack getFluidStack(int amount) {
        Fluid fluid = getFluid();
        return fluid != null ? new FluidStack(fluid, amount) : null;
    }

    public int getForgeDensity() {
        return isGas ? -(int) Math.ceil(densityKgPerM3) : (int) Math.ceil(densityKgPerM3);
    }

    public static Fluid registerFluidFromMaterial(FluidMaterial mat) {
        Fluid fluid = new Fluid(mat.getName()).setDensity(mat.getForgeDensity())
            .setViscosity((int) mat.getViscosity())
            .setTemperature((int) mat.getTemperatureK());

        FluidRegistry.registerFluid(fluid);
        return FluidRegistry.getFluid(mat.getName());
    }
}

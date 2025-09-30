package louis.omoshiroikamo.config;

import net.minecraftforge.common.config.Configuration;

import louis.omoshiroikamo.api.fluid.FluidEntry;

public class FluidConfig {

    public final String displayName;
    public final int meta;
    public final double densityKgPerM3;
    public final double viscosityPaS;
    public final double temperatureK;
    public final int color;
    public final boolean isGas;

    public FluidConfig(String displayName, int meta, double densityKgPerM3, double viscosityPaS, double temperatureK,
        int color, boolean isGas) {
        this.displayName = displayName;
        this.meta = meta;
        this.densityKgPerM3 = densityKgPerM3;
        this.viscosityPaS = viscosityPaS;
        this.temperatureK = temperatureK;
        this.color = color;
        this.isGas = isGas;
    }

    public static FluidConfig defaultFor(String name) {
        int defaultMeta = Config.fluidConigs.size();
        return new FluidConfig(name, defaultMeta, 1000.0, 0.00089, 298.15, 0x3F76E4, false);
    }

    public static FluidConfig loadFromConfig(Configuration config, FluidEntry entry) {
        String cat = Config.sectionFluid.name + "."
            + entry.getName()
                .toLowerCase();

        int meta = entry.defaults.meta;

        double density = config.get(cat, "density", entry.defaults.densityKgPerM3, "Density (kg/m³)")
            .getDouble();
        double viscosity = config.get(cat, "viscosityPaS", entry.defaults.viscosityPaS, "Viscosity (Pa·s)")
            .getDouble();
        double temperature = config.get(cat, "temperatureK", entry.defaults.temperatureK, "Temperature (K)")
            .getDouble();

        int defaultColor = entry.defaults.color;
        String hex = String.format("0x%06X", defaultColor)
            .toUpperCase();
        int color = config.get(cat, "color", defaultColor, "Color as RGB (hex), e.g., " + hex)
            .getInt();

        boolean isGas = config.get(cat, "isGas", entry.defaults.isGas, "Is this fluid a gas?")
            .getBoolean();

        return new FluidConfig(entry.getName(), meta, density, viscosity, temperature, color, isGas);
    }
}

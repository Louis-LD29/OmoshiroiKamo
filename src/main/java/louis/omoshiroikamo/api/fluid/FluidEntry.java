package louis.omoshiroikamo.api.fluid;

import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.config.FluidConfig;

public class FluidEntry {

    private final String name;
    public final int meta;
    public final FluidConfig defaults;

    public FluidEntry(String name, int meta, double densityKgPerM3, double viscosityPaS, double temperatureK, int color,
        boolean isGas) {
        this.name = name;
        this.meta = meta;
        this.defaults = new FluidConfig(name, meta, densityKgPerM3, viscosityPaS, temperatureK, color, isGas);
    }

    public FluidEntry(String name, int meta, FluidConfig config) {
        this.name = name;
        this.meta = meta;
        this.defaults = config;
    }

    public FluidEntry(String name) {
        this(
            name,
            FluidRegistry.all()
                .size(),
            FluidConfig.defaultFor(name));
    }

    public String getName() {
        return name;
    }

    public int getMeta() {
        return meta;
    }

    public String getUnlocalizedName() {
        return name.replace(" ", "");
    }

    public FluidConfig getConfig() {
        return Config.fluidConigs.getOrDefault(name, defaults);
    }

    public double getDensityKgPerM3() {
        return getConfig().densityKgPerM3;
    }

    public double getTemperature() {
        return getConfig().temperatureK;
    }

    public int getColor() {
        int color = getConfig().color;

        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;

        float brightness = 1.2f;
        r = Math.min(255, (int) (r * brightness));
        g = Math.min(255, (int) (g * brightness));
        b = Math.min(255, (int) (b * brightness));

        return (r << 16) | (g << 8) | b;
    }

    public boolean isGas() {
        return getConfig().isGas;
    }
}

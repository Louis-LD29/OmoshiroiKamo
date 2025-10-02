package louis.omoshiroikamo.api.fluid;

public class FluidEntry {

    private final String name;
    public final int meta;
    public final double densityKgPerM3;
    public final double viscosityPaS;
    public final double temperatureK;
    public final int color;
    public final boolean isGas;

    public FluidEntry(String name, int meta, double densityKgPerM3, double viscosityPaS, double temperatureK, int color,
        boolean isGas) {
        this.name = name;
        this.meta = meta;
        this.densityKgPerM3 = densityKgPerM3;
        this.viscosityPaS = viscosityPaS;
        this.temperatureK = temperatureK;
        this.color = color;
        this.isGas = isGas;
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

    public double getDensityKgPerM3() {
        return densityKgPerM3;
    }

    public double getViscosityPaS() {
        return viscosityPaS;
    }

    public double getTemperature() {
        return temperatureK;
    }

    public int getColor() {
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
        return isGas;
    }
}

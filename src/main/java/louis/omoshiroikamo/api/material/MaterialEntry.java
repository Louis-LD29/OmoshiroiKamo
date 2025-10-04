package louis.omoshiroikamo.api.material;

import java.awt.Color;

import louis.omoshiroikamo.api.enums.BlockMassType;
import louis.omoshiroikamo.api.enums.VoltageTier;

public class MaterialEntry {

    public final String name;
    public final int meta;
    public final double densityKgPerM3;
    public final double specificHeatJPerKgK;
    public final double thermalConductivityWPerMK;
    public final double meltingPointK;
    public final double maxPressureMPa;
    public final double electricalConductivity;
    public final int color;
    public final int moltenColor;

    public MaterialEntry(String name, int meta, double densityKgPerM3, double specificHeatJPerKgK,
        double thermalConductivityWPerMK, double meltingPointK, double maxPressureMPa, double electricalConductivity,
        int color, int moltenColor) {
        this.name = name;
        this.meta = meta;
        this.densityKgPerM3 = densityKgPerM3;
        this.specificHeatJPerKgK = specificHeatJPerKgK;
        this.thermalConductivityWPerMK = thermalConductivityWPerMK;
        this.meltingPointK = meltingPointK;
        this.maxPressureMPa = maxPressureMPa;
        this.electricalConductivity = electricalConductivity;
        this.color = color;
        this.moltenColor = moltenColor;
    }

    public int getMeta() {
        return meta;
    }

    public String getName() {
        return name;
    }

    public String getUnlocalizedName() {
        return name.replace(" ", "");
    }

    public double getDensityKgPerM3() {
        return densityKgPerM3;
    }

    public double getSpecificHeat() {
        return specificHeatJPerKgK;
    }

    public double getThermalConductivity() {
        return thermalConductivityWPerMK;
    }

    public double getMeltingPointK() {
        return meltingPointK;
    }

    public double getMaxPressureMPa() {
        return maxPressureMPa;
    }

    public double getElectricalConductivity() {
        return electricalConductivity;
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

    public int getMoltenColor() {

        int r = (moltenColor >> 16) & 0xFF;
        int g = (moltenColor >> 8) & 0xFF;
        int b = moltenColor & 0xFF;

        float[] hsb = Color.RGBtoHSB(r, g, b, null);

        float saturation = Math.min(1.0f, hsb[1] * 1.2f + 0.1f);
        float brightness = Math.min(1.0f, hsb[2] * 1.3f + 0.1f);

        return Color.HSBtoRGB(hsb[0], saturation, brightness);
    }

    // Item
    public int getItemSlotCount() {
        double volume = getVolume();
        return Math.max(1, (int) Math.ceil(volume / 0.01)) * 2;
    }

    // Fluid
    private static final double STANDARD_BLOCK_MASS_KG = 200.0;

    public double getVolume() {
        return STANDARD_BLOCK_MASS_KG / getDensityKgPerM3();
    }

    public int getVolumeMB() {
        int raw = (int) Math.round(getVolume() * 1_000_000);
        return (raw + 250) / 500 * 500;
    }

    // Energy
    private static final double ENERGY_SCALE = 1.0 / 400.0;
    private static final double POWER_TRANSFER_SCALE = 1e-8;
    private static final double POWER_USAGE_SCALE = 1e-3;

    public int getMaxVoltage() {
        double score = Math.pow(getElectricalConductivity(), 0.4) * Math.pow(getThermalConductivity(), 0.3)
            * Math.log(getMeltingPointK());
        return (int) Math.round(score * 0.0025) * 10;
    }

    public VoltageTier getVoltageTier() {
        return VoltageTier.fromVoltage(getMaxVoltage());
    }

    public int getEnergyStorageCapacity() {
        double deltaT = getMeltingPointK() - 300.0;
        double energyJoule = getSpecificHeat() * STANDARD_BLOCK_MASS_KG * deltaT;
        double energyFE = energyJoule * ENERGY_SCALE;
        return (int) Math.round(energyFE / 100.0) * 100;
    }

    public int getMaxPowerTransfer() {
        double power = getElectricalConductivity() * getMaxVoltage()
            * getMaxVoltage()
            * Math.sqrt(getThermalConductivity())
            * getVolume();
        return (int) Math.max(100, Math.round((power * POWER_TRANSFER_SCALE) / 10.0) * 10);
    }

    public int getMaxPowerUsage() {
        double capacity = Math.sqrt(getThermalConductivity()) * getSpecificHeat() * STANDARD_BLOCK_MASS_KG;
        return (int) Math.max(20, Math.round(capacity * POWER_USAGE_SCALE / 10.0) * 10);
    }

    // Block
    public float getHardness() {
        return (float) Math.max(1.0, Math.log10(getDensityKgPerM3()) - 2);
    }

    public float getResistance() {
        return (float) Math.max(5.0, (getMeltingPointK() / 400.0) + (getDensityKgPerM3() / 5000.0));
    }

    public double getMassKg(BlockMassType type) {
        return getDensityKgPerM3() * type.getVolumeM3();
    }

    // TiC
    public int getCooldownTicks() {
        double heatCapacity = getSpecificHeat() * STANDARD_BLOCK_MASS_KG; // J/K
        double deltaT = getMeltingPointK() - 300.0; // Nhiệt độ cần để nóng chảy
        double energyRequired = heatCapacity * deltaT; // J

        double normalized = energyRequired / 200_000.0; // chuẩn hoá để không quá lớn
        int base = 20; // 1 giây
        return (int) Math.max(10, Math.min(200, base + normalized)); // Clamp từ 10 đến 200 ticks
    }

}

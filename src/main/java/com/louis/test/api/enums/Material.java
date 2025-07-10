package com.louis.test.api.enums;

import com.louis.test.common.config.Config;
import com.louis.test.common.config.MaterialConfig;

public enum Material {

    IRON("Iron", 7870, 449, 80.2, 1811, 25, 1.0e7, 0xD8D8D8),
    COPPER("Copper", 8960, 385, 401, 1358, 21, 5.96e7, 0xF08048),
    SILVER("Silver", 10490, 235, 429, 1234, 15, 6.30e7, 0xF0F0F0),
    GOLD("Gold", 19300, 129, 318, 1337, 15, 4.52e7, 0xFFE14A),
    ZINC("Zinc", 7135, 388, 116, 692, 10, 1.69e7, 0xBBBBBB),

    ALUMINUM("Aluminum", 2700, 900, 237, 933, 20, 3.77e7, 0xE5E5E5),
    LEAD("Lead", 11340, 128, 35, 600, 5, 4.80e6, 0x5C5C5C),
    TITANIUM("Titanium", 4507, 522, 21.9, 1941, 50, 2.38e6, 0xC8D8EF),
    CHROMIUM("Chromium", 7190, 449, 93.9, 2180, 40, 7.90e6, 0xB0FF5A),
    NICKEL("Nickel", 8908, 440, 90.9, 1728, 35, 1.43e7, 0xCCCCCC),

    CARBON_STEEL("Carbon Steel", 7850, 486, 50, 1698, 35, 6.00e6, 0x707070),
    STAINLESS_STEEL("Stainless Steel", 8000, 500, 16, 1783, 40, 1.35e6, 0xA0A0A0),
    BRASS("Brass", 8500, 380, 109, 1203, 20, 1.60e7, 0xF2D56B),
    INCONEL("Inconel 625", 8440, 435, 15, 1623, 55, 2.00e6, 0x8A8A8A),
    TUNGSTEN("Tungsten", 19250, 134, 173, 3695, 60, 1.82e7, 0x3A3A3A),
    TUNGSTEN_CARBIDE("Tungsten Carbide", 15000, 180, 84, 3143, 80, 5.00e6, 0x555555),

    NIOBIUM("Niobium", 8570, 265, 54, 2741, 45, 6.70e6, 0xAFAFFF);

    private final String displayName;
    private final double densityKgPerM3;
    private final double specificHeatJPerKgK;
    private final double thermalConductivityWPerMK;
    private final double meltingPointK;
    private final double maxPressureMPa;
    private final double electricalConductivity;
    private final int color;

    private static final double STANDARD_BLOCK_MASS_KG = 200.0;
    private static final double ENERGY_SCALE = 1.0 / 400.0; // từ 40 → 400
    private static final double POWER_TRANSFER_SCALE = 1e-8; // từ 1e-7 → 1e-8
    private static final double POWER_USAGE_SCALE = 1e-3; // từ 1e-5 → 1e-6

    private final MaterialConfig defaults;

    Material(String displayName, double density, double specificHeat, double thermalConductivity, double meltingPoint,
        double maxPressureMPa, double electricalConductivity, int color) {
        this.displayName = displayName;
        this.densityKgPerM3 = density;
        this.specificHeatJPerKgK = specificHeat;
        this.thermalConductivityWPerMK = thermalConductivity;
        this.meltingPointK = meltingPoint;
        this.maxPressureMPa = maxPressureMPa;
        this.electricalConductivity = electricalConductivity;
        this.color = color;
        this.defaults = new MaterialConfig(
            density,
            specificHeat,
            thermalConductivity,
            meltingPoint,
            maxPressureMPa,
            electricalConductivity,
            color);
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getDefaultDensityKgPerM3() {
        return densityKgPerM3;
    }

    public double getDefaultSpecificHeat() {
        return specificHeatJPerKgK;
    }

    public double getDefaultThermalConductivity() {
        return thermalConductivityWPerMK;
    }

    public double getDefaultMeltingPointK() {
        return meltingPointK;
    }

    public double getDefaultMaxPressureMPa() {
        return maxPressureMPa;
    }

    public double getDefaultElectricalConductivity() {
        return electricalConductivity;
    }

    public int getDefaultColor() {
        return color;
    }

    public static Material fromMeta(int meta) {
        Material[] values = values();
        if (meta < 0 || meta >= values.length) return IRON;
        return values[meta];
    }

    public MaterialConfig get() {
        return Config.materialConfigs.getOrDefault(this, defaults);
    }

    public double getDensityKgPerM3() {
        return get().densityKgPerM3;
    }

    public double getSpecificHeat() {
        return get().specificHeatJPerKgK;
    }

    public double getThermalConductivity() {
        return get().thermalConductivityWPerMK;
    }

    public double getMeltingPointK() {
        return get().meltingPointK;
    }

    public double getMaxPressureMPa() {
        return get().maxPressureMPa;
    }

    public double getMassKg(BlockMassType type) {
        return getDensityKgPerM3() * type.getVolumeM3();
    }

    public double getElectricalConductivity() {
        return get().electricalConductivity;
    }

    public int getColor() {
        int r = (get().color >> 16) & 0xFF;
        int g = (get().color >> 8) & 0xFF;
        int b = get().color & 0xFF;

        float brightness = 1.2f;

        r = Math.min(255, (int) (r * brightness));
        g = Math.min(255, (int) (g * brightness));
        b = Math.min(255, (int) (b * brightness));

        return (r << 16) | (g << 8) | b;
    }

    // Item

    public int getItemSlotCount() {
        double volumePerBlock = getVolume();
        double slotVolume = 0.01;
        int slotCount = (int) Math.ceil(volumePerBlock / slotVolume);
        return Math.max(1, slotCount);
    }

    // Fluid

    public double getVolume() {
        return STANDARD_BLOCK_MASS_KG / getDensityKgPerM3();
    }

    public int getVolumeMB() {
        int rawVolumeMB = (int) Math.round(getVolume() * 1_000_000);
        return (rawVolumeMB + 250) / 500 * 500;
    }

    // Energy

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
        double usage = capacity * POWER_USAGE_SCALE;
        return (int) Math.max(20, Math.round(usage / 10.0) * 10);
    }

}

package com.louis.test.api.enums;

public enum Material {

    IRON("Iron", 7870, 449, 80.2, 1811, 25, 1.0e7, 0xC0C0C0),
    COPPER("Copper", 8960, 385, 401, 1358, 21, 5.96e7, 0xD47C4A),
    SILVER("Silver", 10490, 235, 429, 1234, 15, 6.30e7, 0xE0E0E0),
    GOLD("Gold", 19300, 129, 318, 1337, 15, 4.52e7, 0xFFD700),
    ZINC("Zinc", 7135, 388, 116, 692, 10, 1.69e7, 0xA9A9A9),

    ALUMINUM("Aluminum", 2700, 900, 237, 933, 20, 3.77e7, 0xD3D3D3),
    LEAD("Lead", 11340, 128, 35, 600, 5, 4.80e6, 0x4B4B4B),
    TITANIUM("Titanium", 4507, 522, 21.9, 1941, 50, 2.38e6, 0xB0C4DE),
    CHROMIUM("Chromium", 7190, 449, 93.9, 2180, 40, 7.90e6, 0x9ACD32),
    NICKEL("Nickel", 8908, 440, 90.9, 1728, 35, 1.43e7, 0xAAAAAA),

    CARBON_STEEL("Carbon Steel", 7850, 486, 50, 1698, 35, 6.00e6, 0x505050),
    STAINLESS_STEEL("Stainless Steel", 8000, 500, 16, 1783, 40, 1.35e6, 0x8A8A8A),
    BRASS("Brass", 8500, 380, 109, 1203, 20, 1.60e7, 0xE1C16E),
    INCONEL("Inconel 625", 8440, 435, 15, 1623, 55, 2.00e6, 0x666666),
    TUNGSTEN("Tungsten", 19250, 134, 173, 3695, 60, 1.82e7, 0x2E2E2E),
    TUNGSTEN_CARBIDE("Tungsten Carbide", 15000, 180, 84, 3143, 80, 5.00e6, 0x3D3D3D),

    NIOBIUM("Niobium", 8570, 265, 54, 2741, 45, 6.70e6, 0x7F7FFF);

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
    }

    public String getDisplayName() {
        return displayName;
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

    public double getMaxPressureAtm() {
        return getMaxPressureMPa() * 9.86923;
    }

    public double getElectricalConductivity() {
        return electricalConductivity;
    }

    public double getMassKg(BlockMassType type) {
        return getDensityKgPerM3() * type.getVolumeM3();
    }

    public int getColor() {
        return color;
    }

    public static Material fromMeta(int meta) {
        Material[] values = values();
        if (meta < 0 || meta >= values.length) return IRON;
        return values[meta];
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
        double score = Math.pow(electricalConductivity, 0.4) * Math.pow(thermalConductivityWPerMK, 0.3)
            * Math.log(meltingPointK);
        return (int) Math.round(score * 0.0025) * 10;
    }

    public VoltageTier getVoltageTier() {
        return VoltageTier.fromVoltage(getMaxVoltage());
    }

    public int getEnergyStorageCapacity() {
        double deltaT = meltingPointK - 300.0;
        double energyJoule = specificHeatJPerKgK * STANDARD_BLOCK_MASS_KG * deltaT;
        double energyFE = energyJoule * ENERGY_SCALE;
        return (int) Math.round(energyFE / 100.0) * 100;
    }

    public int getMaxPowerTransfer() {
        double power = electricalConductivity * getMaxVoltage()
            * getMaxVoltage()
            * Math.sqrt(thermalConductivityWPerMK)
            * getVolume();
        return (int) Math.max(100, Math.round((power * POWER_TRANSFER_SCALE) / 10.0) * 10);

    }

    public int getMaxPowerUsage() {
        double capacity = Math.sqrt(thermalConductivityWPerMK) * specificHeatJPerKgK * STANDARD_BLOCK_MASS_KG;
        double usage = capacity * POWER_USAGE_SCALE;
        return (int) Math.max(20, Math.round(usage / 10.0) * 10);
    }

}

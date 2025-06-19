package com.louis.test.api.enums;

public enum Material {

    IRON("Iron", 7870, 449, 80.2, 1538, 250, 0.025),
    COPPER("Copper", 8960, 385, 401, 1085, 210, 0.02),
    SILVER("Silver", 10490, 235, 429, 961, 150, 0.015),
    GOLD("Gold", 19300, 129, 318, 1064, 150, 0.012),
    ZINC("Zinc", 7135, 388, 116, 419, 100, 0.03),
    CHROMIUM("Chromium", 7190, 449, 93.9, 1907, 400, 0.025),
    NICKEL("Nickel", 8908, 440, 90.9, 1455, 350, 0.022),
    TUNGSTEN("Tungsten", 19250, 134, 173, 3422, 600, 0.01),
    ALUMINUM("Aluminum", 2700, 900, 237, 660, 200, 0.035),
    LEAD("Lead", 11340, 128, 35, 327, 50, 0.012),
    TITANIUM("Titanium", 4507, 522, 21.9, 1668, 500, 0.028),
    MOLYBDENUM("Molybdenum", 10280, 251, 138, 2623, 550, 0.018),
    VANADIUM("Vanadium", 6110, 489, 30.7, 1910, 300, 0.027),

    // Alloys
    CARBON_STEEL("Carbon Steel", 7850, 486, 50, 1425, 350, 0.025),
    STAINLESS_STEEL("Stainless Steel", 8000, 500, 16, 1510, 400, 0.023),
    BRASS("Brass", 8500, 380, 109, 930, 200, 0.02),
    CUPRONICKEL("Cupronickel", 8900, 380, 30, 1170, 300, 0.022),
    INCONEL("Inconel 625", 8440, 435, 15, 1350, 550, 0.017),
    TITANIUM_ALLOY("Ti-6Al-4V", 4420, 526, 6.7, 1604, 600, 0.03),
    ALUMINUM_ALLOY("Aluminum Alloy", 2800, 900, 130, 580, 250, 0.04),
    TUNGSTEN_CARBIDE("Tungsten Carbide", 15000, 180, 84, 2870, 800, 0.012),
    SILVER_COPPER("Silver-Copper", 10300, 230, 350, 970, 200, 0.017);

    private final String displayName;
    // Mật độ Kg trên M3
    private final double densityKgPerM3;
    // Nhiệt dung riêng J / (Kg * K)
    private final double specificHeatJPerKgK;
    // Độ dẫn nhiệt W / (M * K)
    private final double thermalConductivityWPerMK;
    // Điểm nóng chảy C
    private final double meltingPointC;
    // Áp suất tối đa
    private final double maxPressureMPa;

    // Sức chứa chất lỏng tối đa
    private final double volume;

    Material(String displayName, double density, double specificHeat, double thermalConductivity, double meltingPoint,
        double maxPressureMPa, double volume) {
        this.displayName = displayName;
        this.densityKgPerM3 = density;
        this.specificHeatJPerKgK = specificHeat;
        this.thermalConductivityWPerMK = thermalConductivity;
        this.meltingPointC = meltingPoint;
        this.maxPressureMPa = maxPressureMPa;
        this.volume = volume;
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

    public double getMeltingPointC() {
        return meltingPointC;
    }

    public double getMaxPressureMPa() {
        return maxPressureMPa;
    }

    public double getMaxPressureAtm() {
        return getMaxPressureMPa() * 9.86923;
    }

    public double getVolume() {
        return volume;
    }

    public int getVolumeMB() {
        return (int) Math.round(volume * 1_000_000);
    }

    public double getMassKg(BlockMassType type) {
        return (getDensityKgPerM3() * type.getVolumeM3());
    }
}

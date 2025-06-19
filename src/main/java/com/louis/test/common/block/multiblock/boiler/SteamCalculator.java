package com.louis.test.common.block.multiblock.boiler;

import com.louis.test.api.enums.FluidMaterial;

public class SteamCalculator {

    // Hằng số vật lý
    private static final double ATM_TO_PA = 101325.0;
    private static final double R = 461.5; // J/(kg·K), hằng số khí cho hơi nước
    private static final double DENSITY_WATER = FluidMaterial.WATER.getDensity(); // kg/m³
    private static final double DENSITY_STEAM = FluidMaterial.STEAM.getDensity();; // kg/m³
    private static final double P_BASE = 1.01325;

    private double heatSourceMassKg = 100.0;
    private double heatSourceSpecificHeat = 385.0;
    private double U = 750.0; // hệ số truyền nhiệt U
    private double A = 0.5; // diện tích truyền nhiệt
    private double n = 1;

    // Trạng thái
    private double heatSourceTempC;
    private double waterVolumeMB;
    private double steamVolumeMB;
    private int tankCapacityMB;

    public SteamCalculator() {
        this.heatSourceTempC = 20;
        this.waterVolumeMB = 0;
        this.steamVolumeMB = 0;
        this.tankCapacityMB = 16000;
    }

    // ====== Getter/Setter ======
    public double getWaterVolumeMB() {
        return waterVolumeMB;
    }

    public void setWaterVolumeMB(double waterVolumeMB) {
        this.waterVolumeMB = waterVolumeMB;
    }

    public double getSteamVolumeMB() {
        return steamVolumeMB;
    }

    public void setSteamVolumeMB(double steamVolumeMB) {
        this.steamVolumeMB = steamVolumeMB;
    }

    public int getTankCapacityMB() {
        return tankCapacityMB;
    }

    public void setTankCapacityMB(int tankCapacityMB) {
        this.tankCapacityMB = tankCapacityMB;
    }

    public double getHeatSourceTempC() {
        return heatSourceTempC;
    }

    public void setHeatSourceTempC(double heatSourceTempC) {
        this.heatSourceTempC = heatSourceTempC;
    }

    public void setN(double n) {
        this.n = n;
    }

    // ====== Helper Methods ======

    private double getTankVolumeM3() {
        return tankCapacityMB / 1_000_000.0;
    }

    private double getWaterVolumeM3() {
        return waterVolumeMB / 1_000_000.0;
    }

    private double getSteamVolumeM3() {
        return steamVolumeMB / 1_000_000.0;
    }

    private double getFreeVolumeM3() {
        return getTankVolumeM3() - getWaterVolumeM3();
    }

    public double getPressureAtm() {
        double T_K = heatSourceTempC + 273.15;
        double V = getFreeVolumeM3();

        if (V <= 0.000_000_1) return Double.MAX_VALUE;

        double steamMass = getSteamVolumeM3() * DENSITY_STEAM;
        if (steamMass <= 0) return 0.01;

        double P_Pa = (steamMass * R * T_K) / V;
        return P_Pa / ATM_TO_PA;
    }

    private double simulatedPressureAtm = P_BASE;

    public double getSimulatedPressureAtm() {
        return simulatedPressureAtm;
    }

    public void setSimulatedPressureAtm(double value) {
        this.simulatedPressureAtm = value;
    }

    public double getTboil() {
        double pressure = getPressureAtm();

        double tboil = 100 + 10 * Math.log10(pressure);

        return Math.max(tboil, 0.05);
    }

    public double getLVFromTable() {
        double p = getPressureAtm();

        for (int i = 0; i < LV_TABLE.length - 1; i++) {
            double p1 = LV_TABLE[i][0];
            double p2 = LV_TABLE[i + 1][0];
            if (p >= p1 && p <= p2) {
                double lv1 = LV_TABLE[i][2];
                double lv2 = LV_TABLE[i + 1][2];
                // Tuyến tính theo log(p)
                double t = (Math.log10(p) - Math.log10(p1)) / (Math.log10(p2) - Math.log10(p1));
                return lv1 + (lv2 - lv1) * t;
            }
        }

        // Nếu vượt giới hạn bảng → clamp
        if (p < LV_TABLE[0][0]) return LV_TABLE[0][2];
        return LV_TABLE[LV_TABLE.length - 1][2];
    }

    private double simulatedExpectedSteamStep = 0;

    public double getSimulatedExpectedSteamStep() {
        return simulateSteamMB();
    }

    public void setSimulatedExpectedSteamStep(double value) {
        this.simulatedExpectedSteamStep = value;
    }

    private double[] simulateSteamMassAndMB() {
        if (waterVolumeMB <= 0) return new double[] { 0, 0 };

        double deltaT = heatSourceTempC - getTboil();
        if (deltaT <= 0) return new double[] { 0, 0 };

        double steamMass = (U * n * A * deltaT) / getLVFromTable();
        double waterMass = getWaterVolumeM3() * DENSITY_WATER;
        steamMass = Math.min(steamMass, waterMass);

        double steamM3 = steamMass / DENSITY_STEAM;
        double steamMB = (steamM3 * 1_000_000.0) / 10.0;

        double maxSteamMB = tankCapacityMB - waterVolumeMB;
        steamMB = Math.min(steamMB, maxSteamMB - steamVolumeMB);

        return new double[] { steamMass, steamMB };
    }

    public double simulateWaterUsedMB() {
        double[] result = simulateSteamMassAndMB();
        double steamMassFinal = result[0];
        double waterM3Used = steamMassFinal / DENSITY_WATER;
        return Math.floor(waterM3Used * 1_000_000.0);
    }

    public double simulateSteamMB() {
        double[] result = simulateSteamMassAndMB();
        return Math.floor(result[1]);
    }

    public double calculateSteamStep() {
        double[] result = simulateSteamMassAndMB();
        double steamMass = result[0];
        double steamMB = Math.floor(result[1]);

        // Giới hạn lại steamMass theo steamMB đã bị floor
        double steamM3 = steamMB * 10.0 / 1_000_000.0;
        steamMass = steamM3 * DENSITY_STEAM;

        // Tính lại lượng nước tiêu thụ
        double waterM3Used = steamMass / DENSITY_WATER;
        double waterMBUsed = waterM3Used * 1_000_000.0;

        if (waterMBUsed < 1.0) return 0;

        this.waterVolumeMB -= waterMBUsed;
        this.steamVolumeMB += steamMB;

        // Nhiệt tiêu hao
        double deltaTHeatUp = getTboil() - 20.0;
        double specificHeatWater = 4184.0;
        double Q1 = steamMass * specificHeatWater * deltaTHeatUp;
        double Q2 = steamMass * getLVFromTable();
        double Q = Q1 + Q2;

        double deltaTempDrop = Q / (heatSourceMassKg * heatSourceSpecificHeat);
        this.heatSourceTempC -= deltaTempDrop;

        return steamMB;
    }

    // ====== TABLE ======
    private static final double[][] LV_TABLE = { { 1, 100, 2257000 }, { 2, 120, 2201000 }, { 3, 133, 2163000 },
        { 5, 152, 2108000 }, { 10, 179, 2014000 }, { 20, 212, 1906000 }, { 40, 251, 1785000 }, { 60, 274, 1710000 },
        { 100, 311, 1606000 } };

    // ====== Debug In Console ======
    public void printState(int step, double generatedSteamMB, double deltaTempDrop) {
        System.out.printf(
            "Step %2d - +%.1f mB steam | Water: %.0f mB | Steam: %.0f mB | P: %.5f atm | Tboil: %.2f°C | Tsrc: %.2f°C (-%.2f°C) | Free: %.0f mB%n",
            step,
            generatedSteamMB,
            waterVolumeMB,
            steamVolumeMB,
            getPressureAtm(),
            getTboil(),
            heatSourceTempC,
            deltaTempDrop,
            getFreeVolumeM3() * 1_000_000.0);
    }
}

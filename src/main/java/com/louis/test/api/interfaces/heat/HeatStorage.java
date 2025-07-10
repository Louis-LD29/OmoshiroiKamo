package com.louis.test.api.interfaces.heat;

import net.minecraft.nbt.NBTTagCompound;

import com.louis.test.api.enums.BlockMassType;
import com.louis.test.api.enums.Material;

public class HeatStorage {

    protected float heat;
    protected float heatCapacity;
    protected float maxTemperature;
    protected float maxTransfer;

    // 0K - Không thể thấp hơn
    protected static final float ABSOLUTE_ZERO = 0f;

    // Nhiệt độ môi trường: 20°C = 293K
    protected float normalTemperature = 293f;

    public HeatStorage(float heatCapacity, float maxTemperature, float maxTransfer) {
        this.heatCapacity = heatCapacity;
        this.maxTemperature = maxTemperature;
        this.maxTransfer = maxTransfer;
        this.heat = normalTemperature;
    }

    public HeatStorage(float heatCapacity, float maxTemperature) {
        this(heatCapacity, maxTemperature, heatCapacity);
    }

    public HeatStorage(Material material, BlockMassType type) {
        this(
            calculateHeatCapacity(material, type),
            (float) material.getMeltingPointK(),
            getMaxTransfer(material, type));
    }

    private static float calculateHeatCapacity(Material material, BlockMassType type) {
        return (float) (material.getSpecificHeat() * material.getMassKg(type));
    }

    private static float getMaxTransfer(Material material, BlockMassType type) {
        double k = material.getThermalConductivity(); // W/m·K
        double m = material.getMassKg(type); // kg
        double scaleFactor = 0.05;

        double transferPerTick = k * m * scaleFactor;
        return (float) transferPerTick;
    }

    public HeatStorage readCommon(NBTTagCompound nbt) {
        this.heat = nbt.getFloat("Heat");
        if (this.heat > this.maxTemperature) {
            this.heat = this.maxTemperature;
        } else if (this.heat < ABSOLUTE_ZERO) {
            this.heat = ABSOLUTE_ZERO;
        }
        return this;
    }

    public NBTTagCompound writeCommon(NBTTagCompound nbt) {
        if (this.heat < ABSOLUTE_ZERO) {
            this.heat = ABSOLUTE_ZERO;
        }
        nbt.setFloat("Heat", this.heat);
        return nbt;
    }

    public void setMaxHeatStored(float maxTemperature) {
        this.maxTemperature = maxTemperature;
        if (this.heat > maxTemperature) {
            this.heat = maxTemperature;
        }
    }

    public void setHeatCapacity(float heatCapacity) {
        this.heatCapacity = heatCapacity;
    }

    public void setMaxTransfer(float maxTransfer) {
        this.maxTransfer = maxTransfer;
    }

    public void setHeatStored(float heat) {
        this.heat = Math.max(ABSOLUTE_ZERO, Math.min(this.maxTemperature, heat));
    }

    public void modifyHeatStored(float deltaTemp) {
        this.heat += deltaTemp;
        if (this.heat > this.maxTemperature) {
            this.heat = this.maxTemperature;
        } else if (this.heat < ABSOLUTE_ZERO) {
            this.heat = ABSOLUTE_ZERO;
        }
    }

    public float receiveHeat(float energy, boolean doTransfer) {
        if (energy <= 0) return 0;

        float deltaT = energy / heatCapacity;
        float newTemp = this.heat + deltaT;

        float clampedTemp = Math.max(ABSOLUTE_ZERO, Math.min(newTemp, maxTemperature));
        float actualDeltaT = clampedTemp - this.heat;

        if (doTransfer && actualDeltaT > 0) {
            this.heat = clampedTemp;
        }

        return actualDeltaT * heatCapacity;
    }

    public float extractHeat(float energy, boolean doTransfer) {
        if (energy <= 0) return 0;

        float deltaT = energy / heatCapacity;
        float newTemp = this.heat - deltaT;

        float clampedTemp = Math.max(ABSOLUTE_ZERO, newTemp);
        float actualDeltaT = this.heat - clampedTemp;

        if (doTransfer && actualDeltaT > 0) {
            this.heat = clampedTemp;
        }

        return actualDeltaT * heatCapacity;
    }

    public void updateHeatTowardsNormal() {
        float delta = normalTemperature - heat;

        if (Math.abs(delta) < 0.01f) {
            heat = normalTemperature;
            return;
        }

        float lossFactor = Math.min(maxTransfer, 10f) / 100f;
        float change = Math.signum(delta) * Math.abs(delta) * lossFactor;

        heat += change;

        if (heat > maxTemperature) {
            heat = maxTemperature;
        } else if (heat < ABSOLUTE_ZERO) {
            heat = ABSOLUTE_ZERO;
        }

        this.heat = roundToTwoDecimalPlaces(this.heat);
    }

    public float getMaxTransfer() {
        return maxTransfer;
    }

    public float getHeatStored() {
        return roundToTwoDecimalPlaces(this.heat);
    }

    public float getMaxHeatStored() {
        return this.maxTemperature;
    }

    public float getStoredHeatEnergy() {
        return heatCapacity * (this.heat - normalTemperature);
    }

    public float getHeatCapacity() {
        return heatCapacity;
    }

    private float roundToTwoDecimalPlaces(float value) {
        return Math.round(value * 100.0f) / 100.0f;
    }

    public int calculateTicksToReachTemperature(float maxReceivePerTick, float currentTemp, float targetTemp) {
        if (targetTemp <= currentTemp) return 0;
        if (maxReceivePerTick <= 0 || heatCapacity <= 0) return Integer.MAX_VALUE;

        float deltaT = targetTemp - currentTemp;
        float totalEnergyRequired = heatCapacity * deltaT;

        return (int) Math.ceil(totalEnergyRequired / maxReceivePerTick);
    }

    public String getTimeFormatted(int ticks) {
        if (ticks == Integer.MAX_VALUE) return "Reach";

        int totalSeconds = ticks / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds);
        } else {
            return String.format("%ds", seconds);
        }
    }

    public String calculateTimeToReachTemperature(float maxReceivePerTick, float currentTemp, float targetTemp) {
        int ticks = calculateTicksToReachTemperature(maxReceivePerTick, currentTemp, targetTemp);
        return getTimeFormatted(ticks);
    }
}

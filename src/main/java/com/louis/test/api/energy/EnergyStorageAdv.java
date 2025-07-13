package com.louis.test.api.energy;

import com.louis.test.api.enums.VoltageTier;
import com.louis.test.api.material.MaterialEntry;

import cofh.api.energy.EnergyStorage;

public class EnergyStorageAdv extends EnergyStorage {

    private final MaterialEntry material;

    public EnergyStorageAdv(MaterialEntry material) {
        super(material.getEnergyStorageCapacity(), material.getMaxPowerTransfer());
        this.material = material;
    }

    public EnergyStorageAdv(MaterialEntry material, boolean onlyMaxTransfer) {
        super(
            onlyMaxTransfer ? material.getMaxPowerTransfer() : material.getEnergyStorageCapacity(),
            material.getMaxPowerTransfer());
        this.material = material;
    }

    public MaterialEntry getMaterial() {
        return material;
    }

    public VoltageTier getVoltageTier() {
        return material.getVoltageTier();
    }
}

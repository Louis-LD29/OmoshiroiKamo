package com.louis.test.api.interfaces.energy;

import com.louis.test.api.MaterialEntry;
import com.louis.test.api.enums.VoltageTier;

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

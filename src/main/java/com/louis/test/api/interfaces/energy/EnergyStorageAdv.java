package com.louis.test.api.interfaces.energy;

import com.louis.test.api.enums.Material;
import com.louis.test.api.enums.VoltageTier;

import cofh.api.energy.EnergyStorage;

public class EnergyStorageAdv extends EnergyStorage {

    private final Material material;

    public EnergyStorageAdv(Material material) {
        super(material.getEnergyStorageCapacity(), material.getMaxPowerTransfer());
        this.material = material;
    }

    public EnergyStorageAdv(Material material, boolean onlyMaxTransfer) {
        super(
            onlyMaxTransfer ? material.getMaxPowerTransfer() : material.getEnergyStorageCapacity(),
            material.getMaxPowerTransfer());
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public VoltageTier getVoltageTier() {
        return material.getVoltageTier();
    }
}

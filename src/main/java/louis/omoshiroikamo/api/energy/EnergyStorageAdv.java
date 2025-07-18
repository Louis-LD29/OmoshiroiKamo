package louis.omoshiroikamo.api.energy;

import cofh.api.energy.EnergyStorage;
import louis.omoshiroikamo.api.enums.VoltageTier;
import louis.omoshiroikamo.api.material.MaterialEntry;

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

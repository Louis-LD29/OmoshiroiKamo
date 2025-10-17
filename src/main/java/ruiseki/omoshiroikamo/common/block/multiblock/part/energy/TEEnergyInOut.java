package ruiseki.omoshiroikamo.common.block.multiblock.part.energy;

import ruiseki.omoshiroikamo.api.energy.EnergyStorageAdv;
import ruiseki.omoshiroikamo.api.io.SlotDefinition;
import ruiseki.omoshiroikamo.common.block.abstractClass.AbstractPoweredTE;

public abstract class TEEnergyInOut extends AbstractPoweredTE {

    public TEEnergyInOut(SlotDefinition slotDefinition, EnergyStorageAdv energyStorage) {
        super(slotDefinition, energyStorage);
    }
}

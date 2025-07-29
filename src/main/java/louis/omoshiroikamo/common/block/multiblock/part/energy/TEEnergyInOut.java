package louis.omoshiroikamo.common.block.multiblock.part.energy;

import louis.omoshiroikamo.api.energy.EnergyStorageAdv;
import louis.omoshiroikamo.common.block.abstractClass.AbstractPoweredTE;
import louis.omoshiroikamo.api.io.SlotDefinition;

public abstract class TEEnergyInOut extends AbstractPoweredTE {

    public TEEnergyInOut(SlotDefinition slotDefinition, EnergyStorageAdv energyStorage) {
        super(slotDefinition, energyStorage);
    }
}

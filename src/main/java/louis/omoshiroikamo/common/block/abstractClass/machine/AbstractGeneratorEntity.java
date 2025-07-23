package louis.omoshiroikamo.common.block.abstractClass.machine;

import net.minecraftforge.common.util.ForgeDirection;

import louis.omoshiroikamo.api.energy.IInternalPowerProvider;
import louis.omoshiroikamo.api.material.MaterialRegistry;

public abstract class AbstractGeneratorEntity extends AbstractPoweredMachineEntity implements IInternalPowerProvider {

    public AbstractGeneratorEntity(SlotDefinition slotDefinition) {
        super(slotDefinition, MaterialRegistry.get("Iron"));
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxEnergyStored();
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }
}

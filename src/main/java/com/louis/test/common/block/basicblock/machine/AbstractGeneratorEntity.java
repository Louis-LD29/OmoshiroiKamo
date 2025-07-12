package com.louis.test.common.block.basicblock.machine;

import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.api.energy.IInternalPowerProvider;
import com.louis.test.api.material.MaterialRegistry;

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

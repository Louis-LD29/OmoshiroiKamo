package com.louis.test.common.block.machine;

import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.api.enums.Material;
import com.louis.test.api.interfaces.power.IInternalPowerProvider;

public abstract class AbstractGeneratorEntity extends AbstractPoweredMachineEntity implements IInternalPowerProvider {

    public AbstractGeneratorEntity(SlotDefinition slotDefinition) {
        super(slotDefinition, Material.IRON);
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

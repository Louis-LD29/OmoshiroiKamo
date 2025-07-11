package com.louis.test.common.block.machine;

import net.minecraftforge.common.util.ForgeDirection;

import com.louis.test.api.MaterialEntry;
import com.louis.test.api.interfaces.energy.IInternalPowerReceiver;
import com.louis.test.api.interfaces.energy.PowerHandlerUtil;

public abstract class AbstractPowerConsumerEntity extends AbstractPoweredMachineEntity
    implements IInternalPowerReceiver {

    public AbstractPowerConsumerEntity(SlotDefinition slotDefinition, MaterialEntry material) {
        super(slotDefinition, material);
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (isSideDisabled(from.ordinal())) {
            return 0;
        }
        return PowerHandlerUtil.recieveInternal(this, maxReceive, from, simulate);
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxEnergyStored();
    }
}

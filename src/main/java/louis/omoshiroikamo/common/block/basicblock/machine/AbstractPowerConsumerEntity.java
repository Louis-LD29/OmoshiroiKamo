package louis.omoshiroikamo.common.block.basicblock.machine;

import net.minecraftforge.common.util.ForgeDirection;

import louis.omoshiroikamo.api.energy.IInternalPowerReceiver;
import louis.omoshiroikamo.api.energy.PowerHandlerUtil;
import louis.omoshiroikamo.api.material.MaterialEntry;

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

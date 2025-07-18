package louis.omoshiroikamo.api.energy;

import net.minecraftforge.common.util.ForgeDirection;

import cofh.api.energy.IEnergyConnection;
import louis.omoshiroikamo.api.enums.VoltageTier;

public interface IInternalPoweredTile extends IPowerContainer, IEnergyConnection {

    int getMaxEnergyRecieved(ForgeDirection dir);

    int getMaxEnergyStored();

    VoltageTier getVoltageTier();

    /**
     * Should the power be displayed in WAILA or other places
     */
    boolean displayPower();
}

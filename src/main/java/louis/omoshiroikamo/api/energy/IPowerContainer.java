package louis.omoshiroikamo.api.energy;

import com.enderio.core.common.util.BlockCoord;

public interface IPowerContainer {

    int getEnergyStored();

    void setEnergyStored(int storedEnergy);

    int getMaxEnergyStored();

    BlockCoord getLocation();
}

package com.louis.test.api.interfaces.network;

import com.louis.test.common.block.SmartTank;
import com.louis.test.common.block.boiler.BoilerNetwork;

public interface IBoilerNetwork extends INetwork {

    BoilerNetwork getNetwork();

    void setNetwork(BoilerNetwork network);

    SmartTank getDestroyedNetworkFluidBuffer();

    void setDestroyedNetworkFluidBuffer(SmartTank tank);

    // ✅ Override từ INetwork bằng default method
    @Override
    default boolean canConnectTo(INetwork other) {
        if (other instanceof IBoilerNetwork) {
            return canConnectToBoiler((IBoilerNetwork) other);
        }
        return false;
    }

    // ✅ Subclass sẽ implement cái này
    boolean canConnectToBoiler(IBoilerNetwork other);
}

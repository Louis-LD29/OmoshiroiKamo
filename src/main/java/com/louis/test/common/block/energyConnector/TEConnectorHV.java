package com.louis.test.common.block.energyConnector;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.TargetingInfo;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.WireType;

public class TEConnectorHV extends TEConnectorMV {

    protected boolean canTakeHV() {
        return true;
    }

    @Override
    protected boolean canTakeMV() {
        return false;
    }

    @Override
    public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / 2.0;
        double inset = 0.01;
        return Vec3.createVectorHelper(
            0.5 + fd.offsetX * (inset + conRadius),
            0.5 + fd.offsetY * (inset + conRadius),
            0.5 + fd.offsetZ * (inset + conRadius));
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return limitType == null && super.canConnectCable(cableType, target);
    }

    @Override
    public String getMachineName() {
        return "Connector HV";
    }

}

package com.louis.test.common.block.energyConnector;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;

public class TEConnectorMV extends TEConnectorLV {

    protected boolean canTakeMV() {
        return true;
    }

    protected boolean canTakeLV() {
        return false;
    }

    @Override
    public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / 2.0;
        double inset = 0.1;
        return Vec3.createVectorHelper(
            0.5 + fd.offsetX * (inset + conRadius),
            0.5 + fd.offsetY * (inset + conRadius),
            0.5 + fd.offsetZ * (inset + conRadius));
    }

    @Override
    public String getMachineName() {
        return "Connector MV";
    }

}

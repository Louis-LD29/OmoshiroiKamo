package louis.omoshiroikamo.common.block.energyConnector;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import louis.omoshiroikamo.api.TargetingInfo;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler;
import louis.omoshiroikamo.api.energy.wire.WireType;

public class TEConnectorHV extends TEConnectorMV {

    protected boolean canTakeHV() {
        return true;
    }

    @Override
    protected boolean canTakeMV() {
        return false;
    }

    @Override
    public Vec3 getConnectionOffset(WireNetHandler.Connection con) {
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

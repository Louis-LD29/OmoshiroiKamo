package ruiseki.omoshiroikamo.common.block.energyConnector;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import ruiseki.omoshiroikamo.api.energy.wire.WireNetHandler;

public class TEConnectorLV extends TEConnectorULV {

    protected boolean canTakeLV() {
        return true;
    }

    protected boolean canTakeULV() {
        return false;
    }

    @Override
    public Vec3 getConnectionOffset(WireNetHandler.Connection con) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / 2.0;
        double inset = 0.125;
        return Vec3.createVectorHelper(
            0.5 + fd.offsetX * (inset + conRadius),
            0.5 + fd.offsetY * (inset + conRadius),
            0.5 + fd.offsetZ * (inset + conRadius));
    }

    @Override
    public String getMachineName() {
        return "Connector LV";
    }

}

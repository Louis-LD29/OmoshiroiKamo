package louis.omoshiroikamo.common.block.energyConnector;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.energy.IWireConnectable;
import louis.omoshiroikamo.api.energy.WireNetHandler;
import louis.omoshiroikamo.api.energy.WireType;
import louis.omoshiroikamo.config.Config;

public class TEInsulator extends TEConnectable {

    protected boolean canTakeULV() {
        return true;
    }

    protected boolean canTakeLV() {
        return true;
    }

    protected boolean canTakeMV() {
        return true;
    }

    protected boolean canTakeHV() {
        return true;
    }

    protected boolean canTakeEV() {
        return true;
    }

    protected boolean canTakeIV() {
        return true;
    }

    @Override
    public Vec3 getRaytraceOffset(IWireConnectable link) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        return Vec3.createVectorHelper(.5 + fd.offsetX * .0625, .5 + fd.offsetY * .0625, .5 + fd.offsetZ * .0625);
    }

    @Override
    public Vec3 getConnectionOffset(WireNetHandler.Connection con) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / 2;
        return Vec3
            .createVectorHelper(.5 - conRadius * fd.offsetX, .5 - conRadius * fd.offsetY, .5 - conRadius * fd.offsetZ);
    }

    @SideOnly(Side.CLIENT)
    private AxisAlignedBB renderAABB;

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (renderAABB == null) {
            if (Config.increasedRenderboxes) {
                int inc = getRenderRadiusIncrease();
                renderAABB = AxisAlignedBB.getBoundingBox(
                    xCoord - inc,
                    yCoord - inc,
                    zCoord - inc,
                    xCoord + inc + 1,
                    yCoord + inc + 1,
                    zCoord + inc + 1);
            } else {
                renderAABB = super.getRenderBoundingBox();
            }
        }
        return renderAABB;
    }

    int getRenderRadiusIncrease() {
        return WireType.COPPER.getMaxLength();
    }

    @Override
    public String getMachineName() {
        return "Insulator";
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        super.writeCommon(root);
    }

}

package louis.omoshiroikamo.common.block.energyConnector;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import louis.omoshiroikamo.api.ApiUtils;
import louis.omoshiroikamo.api.TargetingInfo;
import louis.omoshiroikamo.api.energy.wire.IWireConnectable;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler;
import louis.omoshiroikamo.api.energy.wire.WireType;
import louis.omoshiroikamo.config.GeneralConfig;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
public class TETransformer extends TEConnectable {

    WireType secondCable;

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
        WireType wire = con.cableType;
        ForgeDirection facing = ForgeDirection.getOrientation(getFacing());
        ForgeDirection right;

        switch (facing) {
            case NORTH:
                right = ForgeDirection.EAST;
                break;
            case SOUTH:
                right = ForgeDirection.WEST;
                break;
            case EAST:
                right = ForgeDirection.SOUTH;
                break;
            case WEST:
                right = ForgeDirection.NORTH;
                break;
            default:
                return Vec3.createVectorHelper(0.5, 0.5, 0.5);
        }

        double conRadius = wire.getRenderDiameter() / 2.0;
        double offset = conRadius + 0.15;

        if (wire == limitType) {
            return Vec3.createVectorHelper(0.5 + right.offsetX * offset, 0.9, 0.5 + right.offsetZ * offset);
        }

        if (wire == secondCable) {
            return Vec3.createVectorHelper(0.5 - right.offsetX * offset, 1, 0.5 - right.offsetZ * offset);
        }

        return Vec3.createVectorHelper(0.5 + right.offsetX * offset, 0.9, 0.5 + right.offsetZ * offset);
    }

    @SideOnly(Side.CLIENT)
    private AxisAlignedBB renderAABB;

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (renderAABB == null) {
            if (GeneralConfig.increasedRenderboxes) {
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

        if (secondCable != null) {
            root.setString("secondCable", secondCable.getUniqueName());
        }
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);

        if (root.hasKey("secondCable")) {
            secondCable = ApiUtils.getWireTypeFromNBT(root, "secondCable");
        }
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        int tc = getTargetedConnector(cableType);
        switch (tc) {
            case 0:
                return limitType == null || limitType == cableType;

            case 1:
                return secondCable == null || secondCable == cableType;

            default:
                return false;
        }
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target) {
        int tc = getTargetedConnector(cableType);
        if (tc == 0 && limitType == null) {
            limitType = cableType;
        } else if (tc == 1 && secondCable == null) {
            secondCable = cableType;
        }
    }

    @Override
    public void removeCable(WireNetHandler.Connection connection) {
        WireType type = connection != null ? connection.cableType : null;
        if (type == null) {
            limitType = null;
            secondCable = null;
        }
        if (type == limitType) {
            this.limitType = null;
        }
        if (type == secondCable) {
            this.secondCable = null;
        }
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public int getTargetedConnector(WireType cableType) {
        if (limitType == null || limitType == cableType) {
            return 0;
        }

        if (secondCable == null || secondCable == cableType) {
            return 1;
        }

        return -1;
    }

}

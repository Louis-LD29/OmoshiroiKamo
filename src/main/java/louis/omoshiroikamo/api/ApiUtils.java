package louis.omoshiroikamo.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import louis.omoshiroikamo.api.energy.wire.IWCProxy;
import louis.omoshiroikamo.api.energy.wire.IWireConnectable;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler.Connection;
import louis.omoshiroikamo.api.energy.wire.WireType;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is ApiUtils
 */

public class ApiUtils {

    public static ChunkCoordinates toCC(Object object) {
        if (object instanceof ChunkCoordinates) {
            return (ChunkCoordinates) object;
        }
        if (object instanceof TileEntity) {
            return new ChunkCoordinates(
                ((TileEntity) object).xCoord,
                ((TileEntity) object).yCoord,
                ((TileEntity) object).zCoord);
        }
        if (object instanceof IWCProxy) {
            return ((IWCProxy) object).getPos();
        }
        return null;
    }

    public static IWireConnectable toIWC(Object object, World world) {
        if (object instanceof IWireConnectable) {
            return (IWireConnectable) object;
        } else if (object instanceof ChunkCoordinates && world != null
            && world.blockExists(
                ((ChunkCoordinates) object).posX,
                ((ChunkCoordinates) object).posY,
                ((ChunkCoordinates) object).posZ)) {
                    TileEntity te = world.getTileEntity(
                        ((ChunkCoordinates) object).posX,
                        ((ChunkCoordinates) object).posY,
                        ((ChunkCoordinates) object).posZ);
                    if (te instanceof IWireConnectable) {
                        return (IWireConnectable) te;
                    }
                }
        return null;
    }

    public static Vec3 addVectors(Vec3 vec0, Vec3 vec1) {
        return vec0.addVector(vec1.xCoord, vec1.yCoord, vec1.zCoord);
    }

    public static Vec3[] getConnectionCatenary(Connection connection, Vec3 start, Vec3 end) {
        boolean vertical = connection.end.posX == connection.start.posX && connection.end.posZ == connection.start.posZ;

        if (vertical) {
            return new Vec3[] { Vec3.createVectorHelper(end.xCoord, end.yCoord, end.zCoord) };
        }

        double dx = (end.xCoord) - (start.xCoord);
        double dy = (end.yCoord) - (start.yCoord);
        double dz = (end.zCoord) - (start.zCoord);
        double dw = Math.sqrt(dx * dx + dz * dz);
        double k = Math.sqrt(dx * dx + dy * dy + dz * dz) * connection.cableType.getSlack();
        double l = 0;
        int limiter = 0;
        while (!vertical && limiter < 300) {
            limiter++;
            l += 0.01;
            if (Math.sinh(l) / l >= Math.sqrt(k * k - dy * dy) / dw) {
                break;
            }
        }
        double a = dw / 2 / l;
        double p = (0 + dw - a * Math.log((k + dy) / (k - dy))) * 0.5;
        double q = (dy + 0 - k * Math.cosh(l) / Math.sinh(l)) * 0.5;

        int vertices = 16;
        Vec3[] vex = new Vec3[vertices];

        for (int i = 0; i < vertices; i++) {
            float n1 = (i + 1) / (float) vertices;
            double x1 = 0 + dx * n1;
            double z1 = 0 + dz * n1;
            double y1 = a * Math.cosh(((Math.sqrt(x1 * x1 + z1 * z1)) - p) / a) + q;
            vex[i] = Vec3.createVectorHelper(start.xCoord + x1, start.yCoord + y1, start.zCoord + z1);
        }
        vex[vertices - 1] = Vec3.createVectorHelper(end.xCoord, end.yCoord, end.zCoord);

        return vex;
    }

    public static WireType getWireTypeFromNBT(NBTTagCompound tag, String key) {
        // Legacy code for old save data, where types used to be integers
        if (tag.getTag(key) instanceof NBTTagInt) {
            int i = tag.getInteger(key);
            return i == 1 ? WireType.ELECTRUM
                : i == 2 ? WireType.STEEL
                    : i == 3 ? WireType.STRUCTURE_ROPE : i == 4 ? WireType.STRUCTURE_STEEL : WireType.COPPER;
        } else {
            return WireType.getValue(tag.getString(key));
        }
    }
}

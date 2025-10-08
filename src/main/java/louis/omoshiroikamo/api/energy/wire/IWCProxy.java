package louis.omoshiroikamo.api.energy.wire;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import louis.omoshiroikamo.api.TargetingInfo;
import louis.omoshiroikamo.api.energy.wire.WireNetHandler.Connection;
import louis.omoshiroikamo.common.util.Utils;
import louis.omoshiroikamo.common.util.Logger;
/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 * Origin Class is IICProxy
 */

public class IWCProxy implements IWireConnectable {

    private boolean canEnergyPass;
    private int dim;
    private ChunkCoordinates cc;

    public IWCProxy(boolean allowPass, int dimension, ChunkCoordinates pos) {
        canEnergyPass = allowPass;
        dim = dimension;
        cc = pos;
    }

    public IWCProxy(TileEntity te) {
        if (!(te instanceof IWireConnectable)) {
            throw new IllegalArgumentException("Can't create an IWCProxy for a null/non-IWC TileEntity");
        }
        dim = te.getWorldObj().provider.dimensionId;
        canEnergyPass = ((IWireConnectable) te).allowEnergyToPass(null);
        cc = Utils.toCC(te);
    }

    @Override
    public boolean allowEnergyToPass(Connection c) {
        return canEnergyPass;
    }

    public ChunkCoordinates getPos() {
        return cc;
    }

    public int getDimension() {
        return dim;
    }

    @Override
    public void removeCable(Connection connection) {
        // this will load the chunk the TE is in for 1 tick since it needs to be notified about removed wires
        World w = MinecraftServer.getServer()
            .worldServerForDimension(dim);
        if (w == null) {
            Logger.warn("Tried to remove a wire in dimension " + dim + " which does not exist");
            return;
        }
        TileEntity te = w.getTileEntity(cc.posX, cc.posY, cc.posZ);
        if (!(te instanceof IWireConnectable)) {
            return;
        }
        ((IWireConnectable) te).removeCable(connection);
    }

    @Override
    public boolean canConnect() {
        return false;
    }

    @Override
    public boolean isEnergyOutput() {
        return false;
    }

    @Override
    public int outputEnergy(int amount, boolean simulate, int energyType) {
        return 0;
    }

    @Override
    public boolean canConnectCable(WireType cableType, TargetingInfo target) {
        return false;
    }

    @Override
    public void connectCable(WireType cableType, TargetingInfo target) {
    }

    @Override
    public WireType getCableLimiter(TargetingInfo target) {
        return null;
    }

    @Override
    public void onEnergyPassthrough(int amount) {
    }

    @Override
    public Vec3 getRaytraceOffset(IWireConnectable link) {
        return null;
    }

    @Override
    public Vec3 getConnectionOffset(Connection con) {
        return null;
    }

    public static IWCProxy readFromNBT(NBTTagCompound nbt) {
        return new IWCProxy(
            nbt.getBoolean("pass"),
            nbt.getInteger("dim"),
            new ChunkCoordinates(nbt.getInteger("x"), nbt.getInteger("y"), nbt.getInteger("z")));
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound ret = new NBTTagCompound();
        ret.setInteger("dim", dim);
        ret.setInteger("x", cc.posX);
        ret.setInteger("y", cc.posY);
        ret.setInteger("z", cc.posZ);
        ret.setBoolean("pass", canEnergyPass);
        return ret;
    }
}

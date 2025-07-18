package louis.omoshiroikamo.common.block.energyConnector;

import static blusunrize.immersiveengineering.common.util.Utils.toIIC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import blusunrize.immersiveengineering.api.energy.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.AbstractConnection;
import blusunrize.immersiveengineering.api.energy.ImmersiveNetHandler.Connection;
import blusunrize.immersiveengineering.api.energy.WireType;
import blusunrize.immersiveengineering.common.util.Utils;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.energy.tile.IEnergySink;
import louis.omoshiroikamo.common.config.Config;
import louis.omoshiroikamo.common.plugin.compat.IC2Compat;

/*
 * This file contains code adapted from Immersive Engineering by BluSunrize.
 * Original project: https://github.com/BluSunrize/ImmersiveEngineering
 * License: "Blu's License of Common Sense" (https://github.com/BluSunrize/ImmersiveEngineering/blob/master/LICENSE)
 * Modifications made by: [Louis]
 * This adapted version complies with the original license terms.
 * It is intended for use in a standalone mod inspired by Immersive Engineering.
 */
@Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "IC2")
public class TEConnectorULV extends TEConnectable implements IEnergyHandler, IEnergySink {

    boolean inICNet = false;
    private long lastTransfer = -1;
    protected float lastSyncPowerStored = -1;
    public int currentTickAccepted = 0;
    public int energyStored = 0;

    protected boolean canTakeULV() {
        return true;
    }

    @Override
    public Vec3 getRaytraceOffset(IImmersiveConnectable link) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        return Vec3.createVectorHelper(.5 + fd.offsetX * .0625, .5 + fd.offsetY * .0625, .5 + fd.offsetZ * .0625);
    }

    @Override
    public Vec3 getConnectionOffset(ImmersiveNetHandler.Connection con) {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        double conRadius = con.cableType.getRenderDiameter() / 2.0;
        double inset = 0.15;
        return Vec3.createVectorHelper(
            0.5 + fd.offsetX * (inset + conRadius),
            0.5 + fd.offsetY * (inset + conRadius),
            0.5 + fd.offsetZ * (inset + conRadius));
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
            } else renderAABB = super.getRenderBoundingBox();
        }
        return renderAABB;
    }

    int getRenderRadiusIncrease() {
        return WireType.COPPER.getMaxLength();
    }

    @Override
    public String getMachineName() {
        return "Connector ULV";
    }

    @Override
    public void doUpdate() {
        super.doUpdate();
        if (!worldObj.isRemote) {
            if (IC2Compat.isIC2Loaded() && !this.inICNet) {
                IC2Compat.loadIC2Tile(this);
                this.inICNet = true;
            }

            boolean powerChanged = (lastSyncPowerStored != energyStored && shouldDoWorkThisTick(5));
            if (powerChanged) {
                lastSyncPowerStored = energyStored;
                forceClientUpdate = true;
            }

            if (energyStored > 0) {
                int temp = this.transferEnergy(energyStored, true, 0);
                if (temp > 0) {
                    energyStored -= this.transferEnergy(temp, false, 0);
                    markDirty();
                }
            }
            currentTickAccepted = 0;

        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        unload();
    }

    void unload() {
        if (IC2Compat.isIC2Loaded() && this.inICNet) {
            IC2Compat.unloadIC2Tile(this);
            this.inICNet = false;
        }
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        unload();
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        super.writeCommon(root);
        root.setLong("lastTransfer", lastTransfer);
        root.setInteger("stored", energyStored);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        super.readCommon(root);
        lastTransfer = root.getLong("lastTransfer");
        energyStored = root.getInteger("stored");
    }

    @Override
    public boolean isEnergyOutput() {
        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        TileEntity tile = worldObj.getTileEntity(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ);
        return tile != null
            && (tile instanceof IEnergyReceiver || (IC2Compat.isIC2Loaded() && IC2Compat.isEnergySink(tile)));
    }

    @Override
    public int outputEnergy(int amount, boolean simulate, int energyType) {
        int acceptanceLeft = getMaxOutput() - currentTickAccepted;
        if (acceptanceLeft <= 0) return 0;
        int toAccept = Math.min(acceptanceLeft, amount);

        ForgeDirection fd = ForgeDirection.getOrientation(getFacing())
            .getOpposite();
        TileEntity capacitor = worldObj.getTileEntity(xCoord + fd.offsetX, yCoord + fd.offsetY, zCoord + fd.offsetZ);
        int ret = 0;
        if (capacitor instanceof IEnergyReceiver && ((IEnergyReceiver) capacitor).canConnectEnergy(fd.getOpposite()))
            ret = ((IEnergyReceiver) capacitor).receiveEnergy(fd.getOpposite(), toAccept, simulate);
        else if (IC2Compat.isIC2Loaded() && IC2Compat.isAcceptingEnergySink(capacitor, this, fd.getOpposite())) {
            double left = IC2Compat.injectEnergy(
                capacitor,
                fd.getOpposite(),
                IC2Compat.rfToEu(toAccept),
                getVoltageLimit() * getVoltageLimit(),
                simulate);
            ret = toAccept - IC2Compat.euToRf(left);
        }
        if (!simulate) currentTickAccepted += ret;
        return ret;
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (worldObj.isRemote) return 0;
        if (worldObj.getTotalWorldTime() == lastTransfer) return 0;

        int accepted = Math.min(Math.min(getMaxOutput(), getMaxInput()), maxReceive);
        accepted = Math.min(getMaxOutput() - energyStored, accepted);
        if (accepted <= 0) return 0;

        if (!simulate) {
            energyStored += accepted;
            lastTransfer = worldObj.getTotalWorldTime();
            markDirty();
        }

        return accepted;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return getMaxInput();
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    public int transferEnergy(int energy, boolean simulate, final int energyType) {
        int received = 0;
        if (!worldObj.isRemote) {
            Set<AbstractConnection> outputs = ImmersiveNetHandler.INSTANCE
                .getIndirectEnergyConnections(Utils.toCC(this), worldObj);
            int powerLeft = Math.min(Math.min(getMaxOutput(), getMaxInput()), energy);
            final int powerForSort = powerLeft;

            if (outputs.size() < 1) return 0;

            int sum = 0;
            HashMap<AbstractConnection, Integer> powerSorting = new HashMap<AbstractConnection, Integer>();
            for (AbstractConnection con : outputs) {
                IImmersiveConnectable end = toIIC(con.end, worldObj);
                if (con.cableType != null && end != null) {
                    int atmOut = Math.min(powerForSort, con.cableType.getTransferRate() / 10);
                    int tempR = end.outputEnergy(atmOut, true, energyType);
                    if (tempR > 0) {
                        powerSorting.put(con, tempR);
                        sum += tempR;
                    }
                }
            }

            if (sum > 0) for (AbstractConnection con : powerSorting.keySet()) {
                IImmersiveConnectable end = toIIC(con.end, worldObj);
                if (con.cableType != null && end != null) {
                    int output = powerSorting.get(con);

                    int tempR = end
                        .outputEnergy(Math.min(output, con.cableType.getTransferRate() / 10), true, energyType);
                    int r = tempR;
                    int maxInput = getMaxInput();
                    tempR -= (int) Math.max(0, Math.floor(tempR * con.getPreciseLossRate(tempR, maxInput)));
                    end.outputEnergy(tempR, simulate, energyType);
                    HashSet<IImmersiveConnectable> passedConnectors = new HashSet<IImmersiveConnectable>();
                    float intermediaryLoss = 0;
                    for (Connection sub : con.subConnections) {
                        float length = sub.length / (float) sub.cableType.getMaxLength();
                        float baseLoss = (float) sub.cableType.getLossRatio();
                        float mod = (((maxInput - tempR) / (float) maxInput) / .25f) * .1f;
                        intermediaryLoss = MathHelper
                            .clamp_float(intermediaryLoss + length * (baseLoss + baseLoss * mod), 0, 1);

                        int transferredPerCon = ImmersiveNetHandler.INSTANCE
                            .getTransferedRates(worldObj.provider.dimensionId)
                            .getOrDefault(sub, 0);
                        transferredPerCon += r;
                        if (!simulate) {
                            ImmersiveNetHandler.INSTANCE.getTransferedRates(worldObj.provider.dimensionId)
                                .put(sub, transferredPerCon);
                            IImmersiveConnectable subStart = toIIC(sub.start, worldObj);
                            IImmersiveConnectable subEnd = toIIC(sub.end, worldObj);
                            if (subStart != null && passedConnectors.add(subStart))
                                subStart.onEnergyPassthrough((int) (r - r * intermediaryLoss));
                            if (subEnd != null && passedConnectors.add(subEnd))
                                subEnd.onEnergyPassthrough((int) (r - r * intermediaryLoss));
                        }
                    }
                    received += r;
                    powerLeft -= r;
                    if (powerLeft <= 0) break;
                }
            }
        }
        return received;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return from == ForgeDirection.getOrientation(getFacing())
            .getOpposite();
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction) {
        return IC2Compat.isIC2Loaded() && canConnectEnergy(direction);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getDemandedEnergy() {
        return IC2Compat.rfToEu(getMaxInput() - energyStored);
    }

    public int getMaxInput() {
        return getDynamicInputLimit();
    }

    public int getMaxOutput() {
        return getDynamicInputLimit();
    }

    private int getDynamicInputLimit() {
        Set<AbstractConnection> outputs = ImmersiveNetHandler.INSTANCE
            .getIndirectEnergyConnections(Utils.toCC(this), worldObj);

        int max = 0;
        for (AbstractConnection con : outputs) {
            if (con.cableType != null) {
                int rate = con.cableType.getTransferRate() / 10;
                if (rate > max) max = rate;
            }
        }

        return max;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getSinkTier() {
        return getIC2Tier();
    }

    int getIC2Tier() {
        if (this.canTakeIV()) return 4;
        if (this.canTakeEV()) return 4;
        if (this.canTakeHV()) return 3;
        if (this.canTakeMV()) return 2;
        if (this.canTakeLV()) return 1;
        return 0;
    }

    private int getVoltageLimit() {
        switch (getIC2Tier()) {
            case 0:
                return 32;
            case 1:
                return 128;
            case 2:
                return 512;
            case 3:
                return 2048;
            case 4:
                return 8192;
            case 5:
                return 32768;
            default:
                return 32;
        }
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double injectEnergy(ForgeDirection directionFrom, double amount, double voltage) {
        int rf = IC2Compat.euToRf(amount);
        int r = Math.min(getMaxInput() - energyStored, rf);
        energyStored += r;
        double eu = IC2Compat.rfToEu(r);
        return amount - eu;
    }
}

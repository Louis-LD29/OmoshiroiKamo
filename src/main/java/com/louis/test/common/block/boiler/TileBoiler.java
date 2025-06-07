package com.louis.test.common.block.boiler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.FluidUtil;
import com.louis.test.api.enums.IoMode;
import com.louis.test.api.enums.ModObject;
import com.louis.test.api.interfaces.network.IBoilerNetwork;
import com.louis.test.common.block.SmartTank;
import com.louis.test.common.block.machine.AbstractMachineEntity;
import com.louis.test.common.block.machine.SlotDefinition;
import com.louis.test.common.fluid.ModFluids;

public class TileBoiler extends AbstractMachineEntity implements IFluidHandler, IBoilerNetwork {

    protected SmartTank outputTank = new SmartTank(10000);

    SmartTank destroyedNetworkFluidBuffer = null;
    BoilerNetwork network = new BoilerNetwork();

    private static int IO_MB_TICK = 100;

    public TileBoiler() {
        super(new SlotDefinition(0, 1, -1, -1, -1, -1));
    }

    @Override
    public String getMachineName() {
        return ModObject.blockBoiler.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockBoiler.unlocalisedName;
    }

    @Override
    protected boolean isMachineItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    protected boolean processTasks(boolean redstoneCheckPassed) {
        return false;
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        if (network.isValid() && network.shouldSave(this)) {
            network.writeToNBT(nbtRoot);
        }
        outputTank.writeCommon("outputTank", nbtRoot);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        network.readFromNBT(this, nbtRoot);
        outputTank.readCommon("outputTank", nbtRoot);
    }

    @Override
    public void doUpdate() {
        super.doUpdate();
        if (worldObj.isRemote) {
            return;
        }

        if (network.isValid()) {
            if (destroyedNetworkFluidBuffer != null) {
                network.addFluidBuffer(destroyedNetworkFluidBuffer);
                destroyedNetworkFluidBuffer = null;
            }
            network.onUpdate(this);
        }

        if (!network.isValid() || (shouldDoWorkThisTick(20, 1) && network.addToNetwork(this))) {
            findNetwork();
        }
    }

    @Override
    public void invalidate() {
        network.removeFromNetwork(this);
        super.invalidate();
    }

    private void findNetwork() {
        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = new BlockCoord(this).getLocation(dir)
                .getTileEntity(worldObj);
            if (te != null && te instanceof IBoilerNetwork && ((IBoilerNetwork) te).canConnectToBoiler(this)) {
                BoilerNetwork otherNetwork = ((IBoilerNetwork) te).getNetwork();
                if (otherNetwork != null) {
                    otherNetwork.addToNetwork(this);
                }
            }
        }

        if (!network.isValid()) {
            network = new BoilerNetwork(this);
        }
    }

    @Override
    protected boolean doPush(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal())) {
            return false;
        }

        boolean res = super.doPush(dir);
        if (outputTank.getFluidAmount() > 0) {

            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {
                if (target.canFill(
                    dir.getOpposite(),
                    outputTank.getFluid()
                        .getFluid())) {
                    FluidStack push = outputTank.getFluid()
                        .copy();
                    push.amount = Math.min(push.amount, IO_MB_TICK);
                    int filled = target.fill(dir.getOpposite(), push, true);
                    if (filled > 0) {
                        outputTank.drain(filled, true);
                        return res;
                    }
                }
            }
        }
        return res;
    }

    @Override
    protected boolean doPull(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal())) {
            return false;
        }

        boolean res = super.doPull(dir);
        if (network.getInputTank()
            .getFluidAmount()
            < network.getInputTank()
                .getCapacity()) {
            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {

                if (network.getInputTank()
                    .getFluidAmount() > 0) {
                    FluidStack canPull = network.getInputTank()
                        .getFluid()
                        .copy();
                    canPull.amount = network.getInputTank()
                        .getCapacity()
                        - network.getInputTank()
                            .getFluidAmount();
                    canPull.amount = Math.min(canPull.amount, IO_MB_TICK);
                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                    if (drained != null && drained.amount > 0) {
                        network.getInputTank()
                            .fill(drained, true);
                        return res;
                    }
                } else {

                    FluidTankInfo[] infos = target.getTankInfo(dir.getOpposite());
                    if (infos != null) {
                        for (FluidTankInfo info : infos) {
                            if (info.fluid != null && info.fluid.amount > 0) {
                                if (canFill(dir, info.fluid.getFluid())) {
                                    FluidStack canPull = info.fluid.copy();
                                    canPull.amount = Math.min(IO_MB_TICK, canPull.amount);
                                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                                    if (drained != null && drained.amount > 0) {
                                        network.getInputTank()
                                            .fill(drained, true);
                                        return res;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return res;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (!canFill(from)) {
            return 0;
        }
        return fillInternal(resource, doFill);
    }

    int fillInternal(FluidStack resource, boolean doFill) {
        return network.getInputTank()
            .fill(resource, doFill);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return canFill(from) && fluid != null
            && (network.getInputTank()
                .getFluidAmount() > 0
                && network.getInputTank()
                    .getFluid()
                    .getFluidID() == fluid.getID()
                || network.getInputTank()
                    .getFluidAmount() == 0);
    }

    private boolean canFill(ForgeDirection from) {
        IoMode mode = getIoMode(from);
        return mode != IoMode.PUSH && mode != IoMode.DISABLED;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (!canDrain(from)) {
            return null;
        }
        return drainInternal(resource, doDrain);
    }

    FluidStack drainInternal(FluidStack resource, boolean doDrain) {
        return outputTank.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (!canDrain(from)) {
            return null;
        }
        return drainInternal(maxDrain, doDrain);
    }

    FluidStack drainInternal(int maxDrain, boolean doDrain) {
        return outputTank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return canDrain(from) && outputTank.canDrainFluidType(fluid);
    }

    private boolean canDrain(ForgeDirection from) {
        IoMode mode = getIoMode(from);
        return mode != IoMode.PULL && mode != IoMode.DISABLED;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { outputTank.getInfo(), network.getInputTank()
            .getInfo() };
    }

    @Override
    public BlockCoord getLocation() {
        return new BlockCoord(this);
    }

    @Override
    public World getWorldObj() {
        return worldObj;
    }

    @Override
    public BoilerNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(BoilerNetwork network) {
        this.network = network;
    }

    public boolean isMaster() {
        return network.getMaster() == this;
    }

    @Override
    public SmartTank getDestroyedNetworkFluidBuffer() {
        return destroyedNetworkFluidBuffer;
    }

    @Override
    public void setDestroyedNetworkFluidBuffer(SmartTank tank) {
        this.destroyedNetworkFluidBuffer = tank;
    }

    @Override
    public boolean canConnectToBoiler(IBoilerNetwork other) {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings settings) {

        return super.buildUI(data, syncManager, settings).child(
            new FluidSlot()
                .syncHandler(
                    new FluidSlotSyncHandler(network.getInputTank()).canDrainSlot(
                        network.getInputTank()
                            .getFluidAmount() >= 1000
                            && !network.getInputTank()
                                .getFluid()
                                .getFluid()
                                .equals(ModFluids.fluidMana)))
                .topRel(0.2f)
                .leftRel(0.5f));
    }
}

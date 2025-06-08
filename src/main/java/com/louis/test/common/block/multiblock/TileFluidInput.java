package com.louis.test.common.block.multiblock;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.enderio.core.common.util.BlockCoord;
import com.enderio.core.common.util.FluidUtil;
import com.louis.test.api.enums.IoMode;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.SmartTank;
import com.louis.test.common.block.machine.SlotDefinition;

public class TileFluidInput extends TileAddon implements IFluidHandler {

    private final SmartTank tank = new SmartTank(0);

    public TileFluidInput() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1));
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            setIoMode(direction, IoMode.PULL);
        }
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidInput.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockFluidInput.unlocalisedName;
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
    public void doUpdate() {
        super.doUpdate();
        if (worldObj.isRemote) return;

        if (!hasValidController()) {
            findAndSetController();
        }

        if (shouldDoWorkThisTick(20, 0)) {
            if (hasValidController()) {
                getController().onAddonTick(this);
                FluidStack controllerFluid = getController().getInputTank()
                    .getFluid();
                tank.setFluid(controllerFluid);
            } else {
                tank.setFluid(null);
            }
        }
    }

    @Override
    public void invalidateController() {
        super.invalidateController();
        tank.setFluid(null);
    }

    @Override
    protected boolean doPull(ForgeDirection dir) {

        if (isSideDisabled(dir.ordinal())) {
            return false;
        }

        boolean res = super.doPull(dir);
        if (!hasValidController()) {
            return false;
        }
        if (getController().getInputTank()
            .getFluidAmount()
            < getController().getInputTank()
                .getCapacity()) {
            BlockCoord loc = getLocation().getLocation(dir);
            IFluidHandler target = FluidUtil.getFluidHandler(worldObj, loc);
            if (target != null) {

                if (getController().getInputTank()
                    .getFluidAmount() > 0) {
                    FluidStack canPull = getController().getInputTank()
                        .getFluid()
                        .copy();
                    canPull.amount = getController().getInputTank()
                        .getCapacity()
                        - getController().getInputTank()
                            .getFluidAmount();
                    canPull.amount = Math.min(canPull.amount, 1000);
                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                    if (drained != null && drained.amount > 0) {
                        getController().getInputTank()
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
                                    canPull.amount = Math.min(1000, canPull.amount);
                                    FluidStack drained = target.drain(dir.getOpposite(), canPull, true);
                                    if (drained != null && drained.amount > 0) {
                                        getController().getInputTank()
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
        if (!hasValidController()) return 0;
        return getController().getInputTank()
            .fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return hasValidController() && canFill(from)
            && fluid != null
            && (getController().getInputTank()
                .getFluidAmount() > 0
                && getController().getInputTank()
                    .getFluid()
                    .getFluidID() == fluid.getID()
                || getController().getInputTank()
                    .getFluidAmount() == 0);
    }

    private boolean canFill(ForgeDirection from) {
        IoMode mode = getIoMode(from);
        return mode != IoMode.PUSH && mode != IoMode.DISABLED;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return hasValidController() ? new FluidTankInfo[] { getController().getInputTank()
            .getInfo() } : new FluidTankInfo[] { tank.getInfo() };
    }

    @Override
    public void writeCommon(NBTTagCompound nbtRoot) {
        super.writeCommon(nbtRoot);
        tank.writeCommon("tank", nbtRoot);
    }

    @Override
    public void readCommon(NBTTagCompound nbtRoot) {
        super.readCommon(nbtRoot);
        tank.readCommon("tank", nbtRoot);
    }
}

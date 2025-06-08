package com.louis.test.common.block.multiblock;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.louis.test.api.enums.IoMode;
import com.louis.test.api.enums.ModObject;
import com.louis.test.common.block.SmartTank;
import com.louis.test.common.block.boiler.TileBoiler;
import com.louis.test.common.block.machine.SlotDefinition;

public class TileFluidOutput extends TileAddon implements IFluidHandler {

    private final SmartTank tank = new SmartTank(0);

    public TileFluidOutput() {
        super(new SlotDefinition(-1, -1, -1, -1, -1, -1));
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            setIoMode(direction, IoMode.PUSH);
        }
    }

    @Override
    public String getMachineName() {
        return ModObject.blockFluidOutput.unlocalisedName;
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public String getInventoryName() {
        return ModObject.blockFluidOutput.unlocalisedName;
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
                FluidStack controllerFluid = getController().getOutputTank()
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
    public void findAndSetController() {
        if (hasValidController()) {
            getController().unregisterAddon(this);
            setController(null);
        }

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            TileEntity te = worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
            if (te instanceof TileBoiler) {
                setController((TileMain) te);
                getController().registerAddon(this);
                return;
            }

            if (te instanceof TileAddon otherAddon && otherAddon != this && otherAddon.hasValidController()) {
                TileMain potential = otherAddon.getController();
                if (potential instanceof TileBoiler potentialBoiler && !potentialBoiler.isInvalid()) {
                    setController(potentialBoiler);
                    getController().registerAddon(this);
                    return;
                }
            }
        }
    }

    @Override
    public TileBoiler getController() {
        return (TileBoiler) super.getController();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return 0;
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
        return false;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return hasValidController() ? new FluidTankInfo[] { getController().getOutputTank()
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

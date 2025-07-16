package com.louis.test.common.block.multiblock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.louis.test.api.fluid.SmartTank;
import com.louis.test.common.block.AbstractTE;
import com.louis.test.common.block.TileEntityEio;
import com.louis.test.common.block.multiblock.part.fluid.TEFluidInput;
import com.louis.test.common.block.multiblock.part.fluid.TEFluidOutput;
import com.louis.test.common.block.multiblock.part.item.TEItemInput;
import com.louis.test.common.block.multiblock.part.item.TEItemOutput;
import com.louis.test.common.core.helper.Logger;

public abstract class AbstractMultiBlockEntity<T extends AbstractMultiBlockEntity<T>> extends AbstractTE {

    public boolean mMachine = false;
    public boolean mStructureChanged = false;
    public static List<TEFluidInput> mFluidInput = new ArrayList<>();
    public static List<TEFluidOutput> mFluidOutput = new ArrayList<>();
    public static List<TEItemInput> mItemInput = new ArrayList<>();
    public static List<TEItemOutput> mItemOutput = new ArrayList<>();
    public static List<AbstractTE> mTile = new ArrayList<>();

    public abstract IStructureDefinition<T> getStructureDefinition();

    protected abstract String getStructurePieceName();

    public void onStructureChange() {
        mStructureChanged = true;
    }

    public ExtendedFacing getExtendedFacing() {
        return ExtendedFacing.of(ForgeDirection.getOrientation(getFacing()));
    }

    public void clearStructureParts() {
        mFluidInput.clear();
        mFluidOutput.clear();
        mItemInput.clear();
        mItemOutput.clear();
        mTile.clear();
    }

    public boolean checkStructure(boolean aForceReset, AbstractTE te) {
        if (!te.isServerSide()) return mMachine;
        // Only trigger an update if forced (from onPostTick, generally), or if the structure has changed
        if ((mStructureChanged || aForceReset)) {
            clearStructureParts();
        }
        mStructureChanged = false;
        return mMachine;
    }

    public boolean checkMachine(TileEntityEio aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @SuppressWarnings("unchecked")
    protected boolean structureCheck(String piece, int offsetX, int offsetY, int offsetZ) {
        boolean valid = getStructureDefinition().check(
            (T) this,
            piece,
            worldObj,
            getExtendedFacing(),
            xCoord,
            yCoord,
            zCoord,
            offsetX,
            offsetY,
            offsetZ,
            false);

        if (valid && !mMachine) {
            mMachine = true;
            Logger.info("Multiblock formed!");
        } else if (!valid && mMachine) {
            mMachine = false;
            Logger.info("Multiblock broken!");
            clearStructureParts();
        }

        return valid;
    }

    public boolean addToMachine(AbstractTE tile) {
        if (tile == null || mTile.contains(tile)) return false;

        boolean added = false;
        if (tile instanceof TEFluidInput) {
            mFluidInput.add((TEFluidInput) tile);
            added = true;
        }
        if (tile instanceof TEFluidOutput) {
            mFluidOutput.add((TEFluidOutput) tile);
            added = true;
        }
        if (tile instanceof TEItemInput) {
            mItemInput.add((TEItemInput) tile);
            added = true;
        }
        if (added) {
            mTile.add(tile);
        }
        return added;
    }

    public List<FluidStack> getFluidInput() {
        List<FluidStack> list = new ArrayList<>();
        for (TEFluidInput input : mFluidInput) {
            SmartTank[] tanks = input.getTanks();
            if (tanks == null) continue;

            for (SmartTank tank : tanks) {
                if (tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0) {
                    list.add(
                        tank.getFluid()
                            .copy());
                }
            }
        }
        return list;
    }

    public List<ItemStack> getItemInput() {
        List<ItemStack> list = new ArrayList<>();
        for (TEItemInput input : mItemInput) {
            ItemStackHandler inv = input.getInv();
            if (inv == null) continue;

            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.stackSize > 0) {
                    list.add(stack.copy());
                }
            }
        }
        return list;
    }

    public List<FluidStack> getFluidOutput() {
        List<FluidStack> list = new ArrayList<>();
        for (TEFluidOutput output : mFluidOutput) {
            SmartTank[] tanks = output.getTanks();
            if (tanks == null) continue;

            for (SmartTank tank : tanks) {
                if (tank != null && tank.getFluid() != null && tank.getFluidAmount() > 0) {
                    list.add(
                        tank.getFluid()
                            .copy());
                }
            }
        }
        return list;
    }

    public List<ItemStack> getItemOutput() {
        List<ItemStack> list = new ArrayList<>();
        for (TEItemOutput input : mItemOutput) {
            ItemStackHandler inv = input.getInv();
            if (inv == null) continue;

            for (int i = 0; i < inv.getSlots(); i++) {
                ItemStack stack = inv.getStackInSlot(i);
                if (stack != null && stack.stackSize > 0) {
                    list.add(stack.copy());
                }
            }
        }
        return list;
    }

    @Override
    public void writeCommon(NBTTagCompound root) {
        root.setBoolean("mMachine", mMachine);
    }

    @Override
    public void readCommon(NBTTagCompound root) {
        mMachine = root.getBoolean("mMachine");
    }
}

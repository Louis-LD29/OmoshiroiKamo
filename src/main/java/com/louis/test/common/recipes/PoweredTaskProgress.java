package com.louis.test.common.recipes;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public class PoweredTaskProgress implements IPoweredTask {

    private float progress;

    public PoweredTaskProgress(IPoweredTask task) {
        progress = task.getProgress();
    }

    public PoweredTaskProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public void update(float availableEnergy) {}

    @Override
    public boolean isComplete() {
        return getProgress() >= 1;
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public float getRequiredEnergy() {
        return 0;
    }

    @Override
    public float getChance() {
        return 0;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtRoot) {}

    @Override
    public MachineRecipe getRecipe() {
        return null;
    }

    @Override
    public List<ItemStack> getItemOutputs() {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return null;
    }
}

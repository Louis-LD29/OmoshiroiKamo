package com.louis.test.common.block.machine;

import net.minecraft.nbt.NBTTagCompound;

import crazypants.enderio.machine.IMachineRecipe;
import crazypants.enderio.machine.MachineRecipeInput;
import crazypants.enderio.machine.recipe.RecipeBonusType;

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
    public IMachineRecipe.ResultStack[] getCompletedResult() {
        return new IMachineRecipe.ResultStack[0];
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
    public RecipeBonusType getBonusType() {
        return RecipeBonusType.NONE;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtRoot) {}

    @Override
    public IMachineRecipe getRecipe() {
        return null;
    }

    @Override
    public MachineRecipeInput[] getInputs() {
        return new MachineRecipeInput[0];
    }
}

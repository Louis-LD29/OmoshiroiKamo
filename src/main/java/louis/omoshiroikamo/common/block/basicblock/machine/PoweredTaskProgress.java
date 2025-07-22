package louis.omoshiroikamo.common.block.basicblock.machine;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import louis.omoshiroikamo.common.recipes.IPoweredTask;
import louis.omoshiroikamo.common.recipes.MachineRecipe;
import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

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
    public List<ChanceItemStack> getItemOutputs() {
        return null;
    }

    @Override
    public List<ChanceFluidStack> getFluidOutputs() {
        return null;
    }
}

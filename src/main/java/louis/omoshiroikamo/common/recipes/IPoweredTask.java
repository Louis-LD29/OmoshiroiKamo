package louis.omoshiroikamo.common.recipes;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

import louis.omoshiroikamo.common.recipes.chance.ChanceFluidStack;
import louis.omoshiroikamo.common.recipes.chance.ChanceItemStack;

public interface IPoweredTask {

    void update(float availableEnergy);

    boolean isComplete();

    float getProgress();

    float getRequiredEnergy();

    float getChance();

    void writeToNBT(NBTTagCompound nbtRoot);

    @Nullable
    MachineRecipe getRecipe();

    List<ChanceItemStack> getItemOutputs();

    List<ChanceFluidStack> getFluidOutputs();
}

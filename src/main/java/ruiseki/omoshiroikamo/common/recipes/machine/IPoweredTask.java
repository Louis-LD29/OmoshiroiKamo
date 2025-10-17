package ruiseki.omoshiroikamo.common.recipes.machine;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

public interface IPoweredTask {

    void update(float availableEnergy);

    boolean isComplete();

    float getProgress();

    float getRequiredEnergy();

    float getChance();

    void writeToNBT(NBTTagCompound nbtRoot);

    @Nullable
    MachineRecipe getRecipe();

    List<ItemStack> getItemOutputs();

    List<FluidStack> getFluidOutputs();
}

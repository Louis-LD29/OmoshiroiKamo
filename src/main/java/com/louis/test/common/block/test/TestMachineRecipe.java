package com.louis.test.common.block.test;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.louis.test.api.enums.ModObject;

import crazypants.enderio.machine.MachineRecipeInput;
import crazypants.enderio.machine.recipe.AbstractMachineRecipe;
import crazypants.enderio.machine.recipe.IRecipe;

public class TestMachineRecipe extends AbstractMachineRecipe {

    @Override
    public String getUid() {
        return "TestRecipe";
    }

    @Override
    public IRecipe getRecipeForInputs(MachineRecipeInput[] inputs) {
        return TestRecipeManager.instance.getRecipeForInput(inputs);
    }

    @Override
    public boolean isValidInput(MachineRecipeInput input) {
        if (input == null) {
            return false;
        }
        return TestRecipeManager.instance.isValidInput(input);
    }

    @Override
    public String getMachineName() {
        return ModObject.blockTest.unlocalisedName;
    }

    @Override
    public List<MachineRecipeInput> getQuantitiesConsumed(MachineRecipeInput[] inputs) {

        List<MachineRecipeInput> result = new ArrayList<MachineRecipeInput>();

        TestRecipe rec = (TestRecipe) getRecipeForInputs(inputs);
        FluidStack inputFluidStack = rec.getRequiredFluidInput(inputs);
        result.add(new MachineRecipeInput(0, inputFluidStack));

        for (MachineRecipeInput ri : inputs) {
            if (!ri.isFluid() && ri.item != null) {
                ItemStack st = ri.item.copy();
                st.stackSize = rec.getNumConsumed(ri.item);
                result.add(new MachineRecipeInput(ri.slotNumber, st));
            }
        }
        return result;
    }

    @Override
    public ResultStack[] getCompletedResult(float chance, MachineRecipeInput... inputs) {
        if (inputs == null || inputs.length <= 0) {
            return new ResultStack[0];
        }
        TestRecipe recipe = (TestRecipe) getRecipeForInputs(inputs);
        if (recipe == null || !recipe.isValid()) {
            return new ResultStack[0];
        }
        return new ResultStack[] { new ResultStack(recipe.getFluidOutput(inputs)) };
    }
}

package com.louis.test.common.block.test;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.louis.test.api.enums.ModObject;
import com.louis.test.common.recipes.RecipeConfig;
import com.louis.test.common.recipes.RecipeConfigParser;

import crazypants.enderio.machine.MachineRecipeInput;
import crazypants.enderio.machine.MachineRecipeRegistry;
import crazypants.enderio.machine.recipe.IRecipe;
import crazypants.enderio.machine.recipe.Recipe;
import crazypants.enderio.machine.recipe.RecipeInput;
import crazypants.enderio.machine.recipe.RecipeOutput;

public class TestRecipeManager {

    private static final String CORE_FILE_NAME = "TestRecipes_Core.xml";
    private static final String CUSTOM_FILE_NAME = "TestRecipes_User.xml";

    static final TestRecipeManager instance = new TestRecipeManager();

    public static TestRecipeManager getInstance() {
        return instance;
    }

    private final List<TestRecipe> recipes = new ArrayList<TestRecipe>();

    public void loadRecipesFromConfig() {
        RecipeConfig config = RecipeConfig.loadRecipeConfig(CORE_FILE_NAME, CUSTOM_FILE_NAME, null);
        if (config != null) {
            processConfig(config);
        }

        MachineRecipeRegistry.instance.registerRecipe(ModObject.blockTest.unlocalisedName, new TestMachineRecipe());
    }

    public void addCustomRecipes(String xmlDef) {
        RecipeConfig config;
        try {
            config = RecipeConfigParser.parse(xmlDef, null);
        } catch (Exception e) {
            return;
        }

        if (config == null) {
            return;
        }
        processConfig(config);
    }

    public IRecipe getRecipeForInput(MachineRecipeInput[] inputs) {
        if (inputs == null || inputs.length == 0) {
            return null;
        }
        for (IRecipe recipe : recipes) {
            if (recipe.isInputForRecipe(inputs)) {
                return recipe;
            }
        }
        return null;
    }

    private void processConfig(RecipeConfig config) {
        List<Recipe> newRecipes = config.getRecipes(false);
        for (Recipe rec : newRecipes) {
            addRecipe(rec);
        }
    }

    public void addRecipe(IRecipe recipe) {
        if (recipe == null || !recipe.isValid()) {
            return;
        }
        recipes.add(new TestRecipe(recipe));
    }

    public List<TestRecipe> getRecipes() {
        return recipes;
    }

    public boolean isValidInput(MachineRecipeInput input) {
        for (IRecipe recipe : recipes) {
            if (input.item != null && recipe.isValidInput(input.slotNumber, input.item)) {
                return true;
            } else if (input.fluid != null && recipe.isValidInput(input.fluid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidInput(MachineRecipeInput[] inputs) {
        for (IRecipe recipe : recipes) {
            boolean allValid = true;
            for (MachineRecipeInput input : inputs) {
                if (input.item != null) {
                    allValid = recipe.isValidInput(input.slotNumber, input.item);
                } else if (input.fluid != null) {
                    allValid = recipe.isValidInput(input.fluid);
                }
                if (!allValid) {
                    break;
                }
            }
            if (allValid) {
                return true;
            }
        }
        return false;
    }

    public float getMultiplierForInput(Fluid inputFluid, ItemStack input, Fluid output) {
        if (input != null || output != null) {
            for (IRecipe recipe : recipes) {
                RecipeOutput out = recipe.getOutputs()[0];
                RecipeInput in = recipe.getInputs()[recipe.getInputs().length - 1];
                if ((inputFluid == null || in.getFluidInput()
                    .getFluid()
                    .getID() == inputFluid.getID()) && (output == null
                        || out.getFluidOutput()
                            .getFluid()
                            .getID() == output.getID())) {
                    for (RecipeInput ri : recipe.getInputs()) {
                        if (ri.isInput(input)) {
                            return ri.getMulitplier();
                        }
                    }
                }
            }
        }
        // no fluid or not an input for this fluid: best guess
        // (after all, the item IS in the input slot)
        float found = -1f;
        for (IRecipe recipe : recipes) {
            for (RecipeInput ri : recipe.getInputs()) {
                if (ri.isInput(input)) {
                    if (found < 0f || found > ri.getMulitplier()) {
                        found = ri.getMulitplier();
                    }
                }
            }
        }
        return found > 0 ? found : 0;
    }
}

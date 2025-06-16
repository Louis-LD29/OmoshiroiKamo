package com.louis.test.common.block.electrolyzer;

import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.FluidRegistry;

import com.louis.test.api.enums.ModObject;
import com.louis.test.common.fluid.ModFluids;
import com.louis.test.common.recipes.MachineRecipeRegistry;
import com.louis.test.common.recipes.RecipeBuilder;

public class ElectrolyzerRecipes {

    public static void init() {
        System.out.println("Registering Electrolyzer recipes for: " + ModObject.blockElectrolyzer.unlocalisedName);
        MachineRecipeRegistry.register(
            ModObject.blockElectrolyzer.unlocalisedName,
            new RecipeBuilder().addFluidInput(FluidRegistry.WATER, 200)
                .addFluidOutput(ModFluids.fluidHydrogen, 200)
                .addFluidOutput(ModFluids.fluidOxygen, 100));
        MachineRecipeRegistry.register(
            ModObject.blockElectrolyzer.unlocalisedName,
            new RecipeBuilder().addItemInput(Blocks.cactus, 1)
                .addFluidInput(FluidRegistry.LAVA, 200)
                .addFluidOutput(ModFluids.fluidHydrogen, 200)
                .addItemOutput(Blocks.sand, 1));

        MachineRecipeRegistry.register(
            ModObject.blockElectrolyzer.unlocalisedName,
            new RecipeBuilder().addFluidInput(ModFluids.fluidHydrogen, 200)
                .addFluidInput(ModFluids.fluidHydrogen, 100)
                .addFluidOutput(FluidRegistry.WATER, 200));
        System.out.println(
            "Recipes registered: " + MachineRecipeRegistry.getRecipes(ModObject.blockElectrolyzer.unlocalisedName)
                .size());
    }
}

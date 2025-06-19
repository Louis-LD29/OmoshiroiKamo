package com.louis.test.common.recipes;

import net.minecraftforge.common.MinecraftForge;

import com.louis.test.api.enums.ModObject;

public class ModRecipes {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
    }
}

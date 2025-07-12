package com.louis.test.common.recipes;

import com.louis.test.api.ModObject;
import net.minecraftforge.common.MinecraftForge;

public class ModRecipes {

    public static void init() {
        MinecraftForge.EVENT_BUS.register(ManaAnvilRecipe.instance);
        RecipeLoader.loadRecipes(ModObject.blockElectrolyzer.unlocalisedName);
    }
}
